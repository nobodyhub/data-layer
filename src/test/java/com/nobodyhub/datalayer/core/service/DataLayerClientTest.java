package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.service.data.ExecuteRequestData;
import com.nobodyhub.datalayer.core.service.entity.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-26.
 */
public class DataLayerClientTest {
    private static DataLayerServer server;
    private static DataLayerClient client;

    @BeforeClass
    public static void setup() throws IOException {
        server = new DataLayerServer();
        server.start();
        client = new DataLayerClient("localhost");
    }

    @Test
    public void test() throws IOException, InterruptedException {
        List<User> users = client.findAll(User.class);
        assertEquals(3, users.size());
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setFirstName("user1");
        user1.setLastName("Singapore");
        user1.setPassword("user1Pass");
        user1 = client.execute(ExecuteRequestData.create(user1));
        assertEquals("user1", user1.getFirstName());
        assertEquals(users.size() + 1, client.findAll(User.class).size());
        user1 = client.execute(ExecuteRequestData.delete(user1));
        assertEquals("user1", user1.getFirstName());
        assertEquals(users.size(), client.findAll(User.class).size());
    }

    @AfterClass
    public static void destroy() throws Exception {
        server.stop();
        client.close();
    }

}