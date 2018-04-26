package com.nobodyhub.datalayer.core.service;

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
@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DataLayerClientTest {
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

//    @Test
//    public void before() throws IOException, InterruptedException {
//        List<User> users = client.findAll(User.class);
//        assertEquals(3, users.size());
//
//        assertEquals("user1", user1.getFirstName());
//        assertEquals(users.size() + 1, client.findAll(User.class).size());
//        user1 = client.execute(ExecuteRequestData.delete(user1));
//        assertEquals("user1", user1.getFirstName());
//        assertEquals(users.size(), client.findAll(User.class).size());
//    }

    @Benchmark
    public void test() throws IOException, InterruptedException {
        client.findAll(User.class);
    }

    @Test
    public void benchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DataLayerClientTest.class.getSimpleName())
                .warmupIterations(1)
                .measurementIterations(5)
                .forks(1)
                .build();
        Collection<RunResult> runResults = new Runner(opt).run();
        System.out.println(runResults);
    }

    @AfterClass
    public static void destroy() throws Exception {
        server.stop();
        client.close();
    }

}