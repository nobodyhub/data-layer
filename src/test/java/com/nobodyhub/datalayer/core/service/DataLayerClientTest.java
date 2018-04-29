package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.service.common.BenchmarkTest;
import com.nobodyhub.datalayer.core.service.entity.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author yan_h
 * @since 2018-04-26.
 */
public class DataLayerClientTest extends BenchmarkTest {
    private static DataLayerServer server = new DataLayerServer();
    private static DataLayerClient client = new DataLayerClient("localhost");
    private User user1;

    @BeforeClass
    public static void setup() throws IOException {
        server.start();
    }

    @Setup
    public void init() {
        user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setFirstName("user1");
        user1.setLastName("Singapore");
        user1.setPassword("user1Pass");
    }

    @Benchmark
    @Test
    public void test() throws IOException, InterruptedException {
        client.findAll(User.class);
    }

    @AfterClass
    public static void destroy() throws Exception {
        server.stop();
        client.close();
    }

}