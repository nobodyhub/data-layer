package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.data.QueryRequestData;
import com.nobodyhub.datalayer.core.service.data.ResponseData;
import com.nobodyhub.datalayer.core.service.entity.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
public class DataLayerClientApplication {

    public DataLayerClient client;

    @Test
    public void test() {
        QueryRequestData<User> query = new QueryRequestData<>(User.class);
        long start = System.nanoTime();
        ResponseData<List<User>> results = client.query(query);
        System.out.println("Finish in:" + (System.nanoTime() - start) / 1000000);
        assertEquals(3, results.getEntity().size());
        assertEquals(DataLayerProtocol.StatusCode.OK, results.getStatus());
        assertEquals(null, results.getMessage());
    }
}
