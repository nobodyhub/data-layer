package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.service.entity.User;
import com.nobodyhub.datalayer.core.service.repository.DataRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
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
public class DataLayerServerTest {

    private static DataLayerServer server = new DataLayerServer();
    private SessionFactory sessionFactory;
    private DataRepository repo;
    private Session session;
    private User user;

    @Setup
    public void init() throws IOException {
        user = new User();
        user.setEmail("yan_h@example.com");
        user.setLastName("Yan");
        user.setFirstName("Hai");

        sessionFactory = server.getDataLayerServerService().getService().getSessionFactory();
        repo = new DataRepository(sessionFactory);
        session = sessionFactory.openSession();
    }

    @Benchmark
    public void test() {
        repo.query(session, User.class, -1, null, null, null);
    }


    @Test
    public void benchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DataLayerServerTest.class.getSimpleName())
                .warmupIterations(1)
                .measurementIterations(5)
                .forks(1)
                .build();
        Collection<RunResult> runResults = new Runner(opt).run();
        System.out.println(runResults);
    }

    @AfterClass
    public static void destroy() {
        server.stop();
    }


}