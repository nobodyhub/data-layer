package com.nobodyhub.datalayer.core.entity;

import com.nobodyhub.datalayer.core.entity.common.AvroEntity;
import com.nobodyhub.datalayer.core.model.DataLayerProtocol;
import lombok.Data;
import org.junit.Before;
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

    private AvroSchemaLoader loader;
    private AvroSchemaConverter converter;

    @Before
    public void setup() throws ClassNotFoundException {
        loader = new AvroSchemaLoader();
//        loader.preload();
        converter = new AvroSchemaConverter();
    }

    @Test
    public void test() throws IOException, ClassNotFoundException {
        TestEntity avroEntity = new TestEntity();
        avroEntity.setAString("aString");
        avroEntity.setAByteBuffer(ByteBuffer.wrap(new byte[]{1, 2, 3}));
        avroEntity.setAInt(99);
        avroEntity.setAnotherBigDecimal(BigDecimal.TEN.setScale(2));
        DataLayerProtocol.Entity entity = converter.from(avroEntity);
        TestEntity result = converter.to(entity);
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