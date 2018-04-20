package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.PrimitiveClass;
import org.apache.avro.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-20.
 */
public class AvroSchemaLoaderPrimitiveClass {
    @Test
    public void testLoad() {
        AvroSchemaLoader.load(PrimitiveClass.class);
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"aString\",\"type\":[\"string\",\"null\"]},{\"name\":\"aByteBuffer\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"int\",\"null\"]},{\"name\":\"along\",\"type\":[\"long\",\"null\"]},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"afloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"aFloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aboolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimeStamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.PrimitiveClass").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = AvroSchemaLoader.parseClass(PrimitiveClass.class);
        assertEquals(PrimitiveClass.class, record.getClazz());
        assertEquals("PrimitiveClass", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.PrimitiveClass", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases", record.getNamespace());
        assertEquals(16, record.getFields().size());
        for (AvroField field : record.getFields()) {
            checkField(field);
        }
    }

    private void checkField(AvroField field) {
        String fieldName = field.getName();
        switch (fieldName) {
            case "aString": {
                assertEquals("java.lang.String", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(String.class, field.getAvroType().getType());
                assertEquals(Schema.Type.STRING, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                assertEquals(19, field.getAvroType().getPrecision());
                assertEquals(2, field.getAvroType().getScale());
            }
        }
    }
}
