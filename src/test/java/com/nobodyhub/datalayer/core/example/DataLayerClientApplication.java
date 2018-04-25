package com.nobodyhub.datalayer.core.example;

import com.nobodyhub.datalayer.core.service.model.DataLayerClient;
import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.model.QueryRequestData;
import com.nobodyhub.datalayer.core.service.model.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DataLayerServerApplication.class})
@TestPropertySource(locations = "classpath:application.properties")
public class DataLayerClientApplication {

    @Autowired
    public DataLayerClient client;

    @Test
    public void test() {
        QueryRequestData<User> query = new QueryRequestData<>(User.class);
        ResponseData<List<User>> results = client.query(query);
        assertEquals(3, results.getEntity().size());
        assertEquals(DataLayerProtocol.StatusCode.OK, results.getStatus());
        assertEquals(null, results.getMessage());
    }
}
