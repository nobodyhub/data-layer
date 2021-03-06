package com.nobodyhub.datalayer.core.service.common;

import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.util.AvroSchemaConverter;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroVoidEntityTest {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        AvroVoidEntity avroEntity = AvroVoidEntity.get();
        DataLayerProtocol.Entity entity = AvroSchemaConverter.encode(avroEntity);
        AvroVoidEntity result = AvroSchemaConverter.decode(entity);
        assertEquals(avroEntity, result);
    }
}