package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-20.
 */
public class AvroSchemaLoaderPrimitiveContainerClassTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() {
        avroSchemaLoader.load(PrimitiveContainerClass.class);
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveContainerClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"aStringList\",\"type\":[\"null\",{\"type\":\"array\",\"items\":\"string\",\"java-class\":\"java.util.List\"}],\"default\":null},{\"name\":\"aByteBufferList\",\"type\":{\"type\":\"array\",\"items\":\"bytes\",\"java-class\":\"java.util.List\"}},{\"name\":\"aBooleanList\",\"type\":[\"null\",{\"type\":\"array\",\"items\":\"boolean\",\"java-class\":\"java.util.ArrayList\"}],\"default\":null},{\"name\":\"aFloatList\",\"type\":[\"null\",{\"type\":\"array\",\"items\":\"float\",\"java-class\":\"java.util.LinkedList\"}],\"default\":null},{\"name\":\"aIntegerMap\",\"type\":[\"null\",{\"type\":\"map\",\"values\":\"int\"}],\"default\":null},{\"name\":\"aLongMap\",\"type\":[\"null\",{\"type\":\"map\",\"values\":\"long\"}],\"default\":null},{\"name\":\"aDoubleMap\",\"type\":[\"null\",{\"type\":\"map\",\"values\":\"double\"}],\"default\":null},{\"name\":\"aBigDecimalMap\",\"type\":[\"null\",{\"type\":\"map\",\"values\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}}],\"default\":null},{\"name\":\"aUuidSet\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"string\",\"logicalType\":\"uuid\"},\"java-class\":\"java.util.Set\"}],\"default\":null},{\"name\":\"aDateSet\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Date\",\"namespace\":\"java.sql\",\"fields\":[]},\"java-class\":\"java.util.HashSet\"}],\"default\":null},{\"name\":\"aTimestampSet\",\"type\":[\"null\",{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Timestamp\",\"namespace\":\"java.sql\",\"fields\":[]},\"java-class\":\"java.util.TreeSet\"}],\"default\":null}]}",
                AvroData.get().getSchema(PrimitiveContainerClass.class).toString());
    }
}
