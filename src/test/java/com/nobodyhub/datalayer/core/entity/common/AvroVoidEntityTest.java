package com.nobodyhub.datalayer.core.entity.common;

import com.nobodyhub.datalayer.core.entity.AvroSchemaConverter;
import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroVoidEntityTest {
    private AvroSchemaConverter converter = new AvroSchemaConverter();

    @Test
    public void test() throws IOException, ClassNotFoundException {
        AvroVoidEntity avroEntity = AvroVoidEntity.get();
        DataLayerProtocol.Entity entity = converter.encode(avroEntity);
        AvroVoidEntity result = converter.decode(entity);
        assertEquals(avroEntity, result);
    }
}