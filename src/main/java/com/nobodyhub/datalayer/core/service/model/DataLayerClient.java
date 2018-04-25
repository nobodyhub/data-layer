package com.nobodyhub.datalayer.core.service.model;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.entity.AvroSchemaConverter;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Component
public class DataLayerClient implements AutoCloseable {

    @Autowired
    private AvroSchemaConverter converter;
    private ManagedChannel channel;
    private DataLayerServiceGrpc.DataLayerServiceStub asynStub;
    private DataLayerServiceGrpc.DataLayerServiceBlockingStub blockingStub;

    public <T> ResponseData<T> execute(List<ExecuteRequestData<T>> operations) throws IOException, ClassNotFoundException, InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ResponseData<T> responseData = new ResponseData<>();
        StreamObserver<DataLayerProtocol.Response> response = new StreamObserver<DataLayerProtocol.Response>() {
            @Override
            public void onNext(DataLayerProtocol.Response response) {
                responseData.setStatus(response.getErrCode());
                responseData.setMessage(response.getMessage());
                try {
                    responseData.setEntity(converter.to(response.getEntity()));
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                    onError(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                responseData.setStatus(DataLayerProtocol.StatusCode.ERROR);
                responseData.setMessage(t.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };
        StreamObserver<DataLayerProtocol.ExecuteRequest> request = asynStub.execute(response);
        for (ExecuteRequestData<T> operation : operations) {
            DataLayerProtocol.ExecuteRequest reqOp = DataLayerProtocol.ExecuteRequest.newBuilder()
                    .setOpType(operation.getOpType())
                    .setEntity(converter.from(operation.getEntity()))
                    .build();
            request.onNext(reqOp);
        }
        request.onCompleted();
        finishLatch.await();
        return responseData;
    }

    public <T> ResponseData<List<T>> query(QueryRequestData<T> query) {
        ResponseData<List<T>> responseData = new ResponseData<>();
        List<T> data = Lists.newArrayList();
        try {
            DataLayerProtocol.QueryRequest request = DataLayerProtocol.QueryRequest.newBuilder()
                    .setEntityClass(query.getCls().getName())
                    .setCriteria(converter.from(query.getCriteria()))
                    .build();
            Iterator<DataLayerProtocol.Response> iter = blockingStub.query(request);
            while (iter.hasNext()) {
                DataLayerProtocol.Response response = iter.next();
                data.add(converter.to(response.getEntity()));
            }
            responseData.setEntity(data);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            responseData.setStatus(DataLayerProtocol.StatusCode.ERROR);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }


    @Override
    public void close() throws Exception {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
