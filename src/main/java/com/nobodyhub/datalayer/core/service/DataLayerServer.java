package com.nobodyhub.datalayer.core.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Component
public class DataLayerServer {

    @Value("${datalayer.server.port}")
    private int port;
    private Server server;

    @PostConstruct
    public void setup() {
        this.server = ServerBuilder.forPort(port).addService(new DataLayerService()).build();
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