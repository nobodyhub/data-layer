package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.service.common.BenchmarkTest;
import com.nobodyhub.datalayer.core.service.entity.User;
import com.nobodyhub.datalayer.core.service.repository.DataRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author yan_h
 * @since 2018-04-26.
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class DataLayerServerTest extends BenchmarkTest {

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

    @AfterClass
    public static void destroy() {
        server.stop();
    }


}