package com.nobodyhub.datalayer.core.model;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.11.0)",
        comments = "Source: data-layer.proto")
public final class DataLayerServiceGrpc {

    private DataLayerServiceGrpc() {
    }

    public static final String SERVICE_NAME = "datalayer.model.DataLayerService";

    // Static method descriptors that strictly reflect the proto.
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    @java.lang.Deprecated // Use {@link #getExecuteMethod()} instead.
    public static final io.grpc.MethodDescriptor<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request,
            com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse> METHOD_EXECUTE = getExecuteMethodHelper();

    private static volatile io.grpc.MethodDescriptor<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request,
            com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse> getExecuteMethod;

    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    public static io.grpc.MethodDescriptor<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request,
            com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse> getExecuteMethod() {
        return getExecuteMethodHelper();
    }

    private static io.grpc.MethodDescriptor<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request,
            com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse> getExecuteMethodHelper() {
        io.grpc.MethodDescriptor<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request, com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse> getExecuteMethod;
        if ((getExecuteMethod = DataLayerServiceGrpc.getExecuteMethod) == null) {
            synchronized (DataLayerServiceGrpc.class) {
                if ((getExecuteMethod = DataLayerServiceGrpc.getExecuteMethod) == null) {
                    DataLayerServiceGrpc.getExecuteMethod = getExecuteMethod =
                            io.grpc.MethodDescriptor.<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request, com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
                                    .setFullMethodName(generateFullMethodName(
                                            "datalayer.model.DataLayerService", "execute"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new DataLayerServiceMethodDescriptorSupplier("execute"))
                                    .build();
                }
            }
        }
        return getExecuteMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static DataLayerServiceStub newStub(io.grpc.Channel channel) {
        return new DataLayerServiceStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static DataLayerServiceBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        return new DataLayerServiceBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static DataLayerServiceFutureStub newFutureStub(
            io.grpc.Channel channel) {
        return new DataLayerServiceFutureStub(channel);
    }

    /**
     */
    public static abstract class DataLayerServiceImplBase implements io.grpc.BindableService {

        /**
         */
        public io.grpc.stub.StreamObserver<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request> execute(
                io.grpc.stub.StreamObserver<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse> responseObserver) {
            return asyncUnimplementedStreamingCall(getExecuteMethodHelper(), responseObserver);
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            getExecuteMethodHelper(),
                            asyncClientStreamingCall(
                                    new MethodHandlers<
                                            com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request,
                                            com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse>(
                                            this, METHODID_EXECUTE)))
                    .build();
        }
    }

    /**
     */
    public static final class DataLayerServiceStub extends io.grpc.stub.AbstractStub<DataLayerServiceStub> {
        private DataLayerServiceStub(io.grpc.Channel channel) {
            super(channel);
        }

        private DataLayerServiceStub(io.grpc.Channel channel,
                                     io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected DataLayerServiceStub build(io.grpc.Channel channel,
                                             io.grpc.CallOptions callOptions) {
            return new DataLayerServiceStub(channel, callOptions);
        }

        /**
         */
        public io.grpc.stub.StreamObserver<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Request> execute(
                io.grpc.stub.StreamObserver<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse> responseObserver) {
            return asyncClientStreamingCall(
                    getChannel().newCall(getExecuteMethodHelper(), getCallOptions()), responseObserver);
        }
    }

    /**
     */
    public static final class DataLayerServiceBlockingStub extends io.grpc.stub.AbstractStub<DataLayerServiceBlockingStub> {
        private DataLayerServiceBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private DataLayerServiceBlockingStub(io.grpc.Channel channel,
                                             io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected DataLayerServiceBlockingStub build(io.grpc.Channel channel,
                                                     io.grpc.CallOptions callOptions) {
            return new DataLayerServiceBlockingStub(channel, callOptions);
        }
    }

    /**
     */
    public static final class DataLayerServiceFutureStub extends io.grpc.stub.AbstractStub<DataLayerServiceFutureStub> {
        private DataLayerServiceFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private DataLayerServiceFutureStub(io.grpc.Channel channel,
                                           io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected DataLayerServiceFutureStub build(io.grpc.Channel channel,
                                                   io.grpc.CallOptions callOptions) {
            return new DataLayerServiceFutureStub(channel, callOptions);
        }
    }

    private static final int METHODID_EXECUTE = 0;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final DataLayerServiceImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(DataLayerServiceImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_EXECUTE:
                    return (io.grpc.stub.StreamObserver<Req>) serviceImpl.execute(
                            (io.grpc.stub.StreamObserver<com.nobodyhub.datalayer.core.model.DataLayerProtocol.Reponse>) responseObserver);
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class DataLayerServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        DataLayerServiceBaseDescriptorSupplier() {
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return com.nobodyhub.datalayer.core.model.DataLayerProtocol.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("DataLayerService");
        }
    }

    private static final class DataLayerServiceFileDescriptorSupplier
            extends DataLayerServiceBaseDescriptorSupplier {
        DataLayerServiceFileDescriptorSupplier() {
        }
    }

    private static final class DataLayerServiceMethodDescriptorSupplier
            extends DataLayerServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        DataLayerServiceMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (DataLayerServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new DataLayerServiceFileDescriptorSupplier())
                            .addMethod(getExecuteMethodHelper())
                            .build();
                }
            }
        }
        return result;
    }
}
