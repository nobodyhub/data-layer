package com.nobodyhub.datalayer.core.service.util;

import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.common.BenchmarkTest;
import com.nobodyhub.datalayer.core.service.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-27.
 */
public class AvroSchemaConverterTest extends BenchmarkTest {
    private User user;

    @Setup
    @Before
    public void setup() {
        user = new User();
        user.setEmail("Email");
        user.setLastName("Yan");
        user.setFirstName("Hai");
        user.setPassword("password");
        //user.id is null
    }

    @Benchmark
    public User testPerformance() throws IOException, ClassNotFoundException {
        DataLayerProtocol.Entity entity = AvroSchemaConverter.encode(user);
        return AvroSchemaConverter.decode(entity);
    }

    @Test
    public void test() throws IOException, ClassNotFoundException {
        assertEquals(user, testPerformance());
    }

}