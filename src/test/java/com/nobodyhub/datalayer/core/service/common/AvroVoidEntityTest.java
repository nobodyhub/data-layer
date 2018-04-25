package com.nobodyhub.datalayer.core.service.common;

import com.nobodyhub.datalayer.core.service.util.AvroSchemaConverter;
import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.common.AvroVoidEntity;
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