package com.nobodyhub.datalayer.core.proto;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.nobodyhub.datalayer.core.service.common.BenchmarkTest;
import com.nobodyhub.datalayer.core.service.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class ProtoDataTest extends BenchmarkTest {
    public final ProtoData protoData = new ProtoData();
    public final User user = new User();

    @Before
    @Setup
    public void setup() {
        user.setEmail("yan@example.com");
        user.setFirstName("Yan");
        user.setLastName("Hai");
        user.setPassword("password");
    }

    @Test
    public void testCreateProto() throws IllegalAccessException, ExecutionException, Descriptors.DescriptorValidationException, InvalidProtocolBufferException, InstantiationException {
        DynamicMessage userMsg = protoData.encode(user);

        assertEquals(userMsg, userMsg.getParserForType().parseFrom(userMsg.toByteString()));

        User result = new User();
        protoData.decode(result, userMsg.toByteString());
        assertEquals(user, result);
    }

    @Benchmark
    public void testPerformance() throws IllegalAccessException, ExecutionException, Descriptors.DescriptorValidationException, InvalidProtocolBufferException, InstantiationException {
        ByteString data = protoData.encode(user).toByteString();
        protoData.decode(new User(), data);
    }

}