package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.within.ComplexEnum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderComplexEnumTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() {
        avroSchemaLoader.load(ComplexEnum.class);
        assertEquals("{\"type\":\"enum\",\"name\":\"ComplexEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.cases.within\",\"symbols\":[\"C1\",\"C2\",\"C3\"]}",
                AvroData.get().getSchema(ComplexEnum.class).toString());
    }

}