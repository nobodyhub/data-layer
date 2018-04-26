package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.service.entity.User;
import com.nobodyhub.datalayer.core.service.repository.DataRepository;
import org.hibernate.SessionFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * @author yan_h
 * @since 2018-04-26.
 */
public class DataLayerServerTest {
    @Test
    public void test() throws IOException {
        DataLayerServer server = new DataLayerServer(9101);
        server.start();
        SessionFactory sessionFactory = server.getDataLayerServerService().getService().getSessionFactory();
        DataRepository repo = new DataRepository(sessionFactory);
        User user = new User();
        user.setEmail("yan_h@example.com");
        user.setLastName("Yan");
        user.setFirstName("Hai");
        long start = System.nanoTime();
        user = repo.create(sessionFactory.openSession(), user);
        System.out.println("Finish within:" + ((System.nanoTime() - start) / 1000000));
//        server.blockUntilShutdown();
    }
}