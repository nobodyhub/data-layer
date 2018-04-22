package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.PrimitiveClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-20.
 */
public class AvroSchemaLoaderPrimitiveClassTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() {
        avroSchemaLoader.load(PrimitiveClass.class);
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"aString\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"aByteBuffer\",\"type\":[\"null\",\"bytes\"],\"default\":null},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"along\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"aLong\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"afloat\",\"type\":[\"null\",\"float\"],\"default\":null},{\"name\":\"aFloat\",\"type\":[\"null\",\"float\"],\"default\":null},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"aboolean\",\"type\":[\"null\",\"boolean\"],\"default\":null},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":[\"null\",{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":2}],\"default\":null},{\"name\":\"aDate\",\"type\":{\"type\":\"record\",\"name\":\"Date\",\"namespace\":\"java.sql\",\"fields\":[]}},{\"name\":\"aTimeStamp\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"Timestamp\",\"namespace\":\"java.sql\",\"fields\":[]}],\"default\":null},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}",
                AvroData.get().getSchema(PrimitiveClass.class).toString());
    }
}
