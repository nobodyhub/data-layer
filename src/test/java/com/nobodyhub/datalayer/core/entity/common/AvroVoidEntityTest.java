package com.nobodyhub.datalayer.core.entity.common;

import com.nobodyhub.datalayer.core.entity.AvroSchemaConverter;
import com.nobodyhub.datalayer.core.entity.AvroSchemaConverterTest;
import com.nobodyhub.datalayer.core.service.model.DataLayerProtocol;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroVoidEntityTest {
    private AvroSchemaConverter converter = new AvroSchemaConverter();

    @Test
    public void test() throws IOException, ClassNotFoundException {
        AvroVoidEntity avroEntity = AvroVoidEntity.get();
        DataLayerProtocol.Entity entity = converter.from(avroEntity);
        AvroVoidEntity result = converter.to(entity);
        assertEquals(avroEntity, result);
    }
}