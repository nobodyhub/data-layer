package com.nobodyhub.datalayer.core.proto;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.service.repository.DataService;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Ryan
 */
public class DataLayerService extends DataLayerServiceGrpc.DataLayerServiceImplBase {

    @Getter
    private final DataService service;

    public DataLayerService(SessionFactory sessionFactory) {
        service = new DataService(sessionFactory);
    }

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