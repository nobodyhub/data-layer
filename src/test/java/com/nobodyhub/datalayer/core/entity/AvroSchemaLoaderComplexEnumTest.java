package com.nobodyhub.datalayer.core.entity;

import com.nobodyhub.datalayer.core.entity.cases.within.ComplexEnum;
import com.nobodyhub.datalayer.core.entity.data.AvroData;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderComplexEnumTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() {
        avroSchemaLoader.load(ComplexEnum.class);
        Assert.assertEquals("{\"type\":\"enum\",\"name\":\"ComplexEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.entity.cases.within\",\"symbols\":[\"C1\",\"C2\",\"C3\"]}",
                AvroData.get().getSchema(ComplexEnum.class).toString());
    }

}