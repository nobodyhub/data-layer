package com.nobodyhub.datalayer.core.service.util;

import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import lombok.Data;
import org.junit.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroSchemaConverterTest {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        TestEntity avroEntity = new TestEntity();
        avroEntity.setAString("aString");
        avroEntity.setAByteBuffer(ByteBuffer.wrap(new byte[]{1, 2, 3}));
        avroEntity.setAInt(99);
        avroEntity.setAnotherBigDecimal(BigDecimal.TEN.setScale(2));
        DataLayerProtocol.Entity entity = AvroSchemaConverter.encode(avroEntity);
        TestEntity result = AvroSchemaConverter.decode(entity);
        assertEquals(avroEntity, result);
    }

    @Entity
    @Data
    private static class TestEntity implements AvroEntity {
        @Column
        protected String aString;
        @Column
        protected ByteBuffer aByteBuffer;
        @Column(nullable = false)
        protected int aInt;
        @Column(precision = 10, scale = 3)
        protected BigDecimal aBigDecimal;
        @Column(nullable = false)
        protected BigDecimal anotherBigDecimal;
    }
}