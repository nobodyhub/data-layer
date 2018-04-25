package com.nobodyhub.datalayer.core.service;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.db.EntityService;
import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.proto.DataLayerServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author Ryan
 */
@Component
public class DataLayerService extends DataLayerServiceGrpc.DataLayerServiceImplBase {

    @Autowired
    private EntityService service;

    @Override
    public StreamObserver<DataLayerProtocol.ExecuteRequest> execute(StreamObserver<DataLayerProtocol.Response> responseObserver) {
        return new StreamObserver<DataLayerProtocol.ExecuteRequest>() {
            List<DataLayerProtocol.ExecuteRequest> operations = Lists.newArrayList();

            @Override
            public void onNext(DataLayerProtocol.ExecuteRequest value) {
                operations.add(value);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onNext(DataLayerProtocol.Response.newBuilder()
                        .setStatusCode(DataLayerProtocol.StatusCode.ERROR)
                        .setMessage(t.getMessage())
                        .build());
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                try {
                    responseObserver.onNext(DataLayerProtocol.Response.newBuilder()
                            .setStatusCode(DataLayerProtocol.StatusCode.OK)
                            .setEntity(service.execute(operations))
                            .build());
                    responseObserver.onCompleted();
                } catch (IOException | ClassNotFoundException e) {
                    onError(e);
                }
            }
        };
    }

    @Override
    public void query(DataLayerProtocol.QueryRequest request, StreamObserver<DataLayerProtocol.Response> responseObserver) {
        try {
            List<DataLayerProtocol.Entity> entities = service.query(Class.forName(request.getEntityClass()), request.getCriteria());
            for (DataLayerProtocol.Entity entity : entities) {
                responseObserver.onNext(DataLayerProtocol.Response.newBuilder()
                        .setStatusCode(DataLayerProtocol.StatusCode.OK)
                        .setEntity(entity)
                        .build());
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            responseObserver.onNext(DataLayerProtocol.Response.newBuilder()
                    .setStatusCode(DataLayerProtocol.StatusCode.ERROR)
                    .setMessage(e.getMessage())
                    .build());
        } finally {
            responseObserver.onCompleted();
        }
    }
}