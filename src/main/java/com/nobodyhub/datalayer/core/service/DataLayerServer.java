package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.proto.DataLayerService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DataLayerService dataLayerService;

    @PostConstruct
    public void setup() throws IOException {
        this.server = ServerBuilder.forPort(port).addService(dataLayerService).build();
        start();
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