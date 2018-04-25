package com.nobodyhub.datalayer.core.service.model;

import com.nobodyhub.datalayer.core.db.EntityTransaction;
import com.nobodyhub.datalayer.core.entity.AvroSchemaConverter;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Ryan
 */
public class DataLayerService extends DataLayerServiceGrpc.DataLayerServiceImplBase {
    private static final Logger logger = Logger.getLogger(DataLayerService.class.getSimpleName());
    private AvroSchemaConverter converter;

    @Override
    public StreamObserver<DataLayerProtocol.ExecuteRequest> execute(StreamObserver<DataLayerProtocol.Response> responseObserver) {
        return new StreamObserver<DataLayerProtocol.ExecuteRequest>() {
            EntityTransaction transaction = new EntityTransaction(converter);

            @Override
            public void onNext(DataLayerProtocol.ExecuteRequest value) {
                //TODO: temporary save DB operations and execute/commit onCompleted
                transaction.addRequest(value);
            }

            @Override
            public void onError(Throwable t) {
                //TODO: return the error and decide what to do next(retry or abort)
                transaction.onError(t);
            }

            @Override
            public void onCompleted() {
                //TODO: try to commit the operations
                // either return the result or the error
                try {
                    responseObserver.onNext(transaction.execute());
                    responseObserver.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

    }
}