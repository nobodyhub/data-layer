package com.nobodyhub.datalayer.core.proto;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.service.data.RequestData;
import com.nobodyhub.datalayer.core.service.data.ResponseData;
import com.nobodyhub.datalayer.core.service.util.AvroSchemaConverter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.Getter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
public class DataLayerClientService {

    @Getter
    private final String host;
    @Getter
    private final int port;

    private final ManagedChannel channel;
    private final DataLayerServiceGrpc.DataLayerServiceStub asyncStub;
    private final DataLayerServiceGrpc.DataLayerServiceBlockingStub blockingStub;

    public DataLayerClientService(String host, int port) {
        this.host = host;
        this.port = port;
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(host, port).usePlaintext();
        this.channel = channelBuilder.build();
        this.asyncStub = DataLayerServiceGrpc.newStub(channel);
        this.blockingStub = DataLayerServiceGrpc.newBlockingStub(channel);
    }

    public ResponseData execute(RequestData... operations) throws IOException, InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        ResponseData<?> responseData = new ResponseData<>();
        StreamObserver<DataLayerProtocol.Response> response = new StreamObserver<DataLayerProtocol.Response>() {
            @Override
            public void onNext(DataLayerProtocol.Response response) {
                responseData.setStatus(response.getStatusCode());
                responseData.setMessage(response.getMessage());
                try {
                    responseData.setEntity(AvroSchemaConverter.decode(response.getEntity()));
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
        StreamObserver<DataLayerProtocol.Request> request = asyncStub.execute(response);
        for (RequestData operation : operations) {
            DataLayerProtocol.Request reqOp = DataLayerProtocol.Request.newBuilder()
                    .setOpType(operation.getOpType())
                    .setEntity(AvroSchemaConverter.encode(operation.getEntity()))
                    .setCriteria(AvroSchemaConverter.encode(operation.getCriteria()))
                    .build();
            request.onNext(reqOp);
        }
        request.onCompleted();
        finishLatch.await();
        return responseData;
    }

    public <T> ResponseData<List<T>> query(RequestData<T> query) {
        ResponseData<List<T>> responseData = new ResponseData<>();
        List<T> data = Lists.newArrayList();
        try {
            DataLayerProtocol.Request request = DataLayerProtocol.Request.newBuilder()
                    .setEntityClass(query.getCls().getName())
                    .setCriteria(AvroSchemaConverter.encode(query.getCriteria()))
                    .build();
            Iterator<DataLayerProtocol.Response> iter = blockingStub.query(request);
            while (iter.hasNext()) {
                DataLayerProtocol.Response response = iter.next();
                data.add(AvroSchemaConverter.decode(response.getEntity()));
            }
            responseData.setStatus(DataLayerProtocol.StatusCode.OK);
            responseData.setEntity(data);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            responseData.setStatus(DataLayerProtocol.StatusCode.ERROR);
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    public void close() throws Exception {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
