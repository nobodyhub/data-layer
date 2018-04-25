package com.nobodyhub.datalayer.core.service.util;

import com.nobodyhub.datalayer.core.service.util.cases.SimpleEnum;
import com.nobodyhub.datalayer.core.avro.AvroData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderSimpleEnumTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() {
        avroSchemaLoader.load(SimpleEnum.class);
        Assert.assertEquals("{\"type\":\"enum\",\"name\":\"SimpleEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.entity.cases\",\"symbols\":[\"S1\",\"S2\",\"S3\"]}",
                AvroData.get().getSchema(SimpleEnum.class).toString());
    }
}