package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.SimpleEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderSimpleEnumTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() {
        avroSchemaLoader.load(SimpleEnum.class);
        assertEquals("{\"type\":\"enum\",\"name\":\"SimpleEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"symbols\":[\"S1\",\"S2\",\"S3\"]}",
                AvroData.get().getSchema(SimpleEnum.class).toString());
    }
}