package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.proto.DataLayerServerService;
import com.nobodyhub.datalayer.core.service.common.DataLayerConst;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Getter;

import java.io.IOException;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
public class DataLayerServer {

    @Getter
    private final int port;
    private final Server server;

    @Getter
    private final DataLayerServerService dataLayerServerService;


    public DataLayerServer() {
        this(DataLayerConst.DEFAULT_PORT);
    }

    public DataLayerServer(int port) {
        this(port, new DataLayerServerService());
    }

    public DataLayerServer(int port, DataLayerServerService dataLayerServerService) {
        this.port = port;
        this.dataLayerServerService = dataLayerServerService;
        this.server = ServerBuilder.forPort(port).addService(dataLayerServerService).build();
    }

    public void start() throws IOException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                DataLayerServer.this.stop();
            }
        });
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}