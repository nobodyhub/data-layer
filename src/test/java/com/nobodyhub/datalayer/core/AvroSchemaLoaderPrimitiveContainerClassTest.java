package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.PrimitiveClass;
import com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass;
import org.apache.avro.Schema;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-20.
 */
public class AvroSchemaLoaderPrimitiveContainerClassTest {
    @Test
    public void testLoad() {
        AvroSchemaLoader.load(PrimitiveContainerClass.class);
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveContainerClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"aStringList\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"aByteBufferList\",\"type\":[{\"type\":\"array\",\"items\":\"bytes\"},\"null\"]},{\"name\":\"aBooleanList\",\"type\":[{\"type\":\"array\",\"items\":\"boolean\"},\"null\"]},{\"name\":\"aFloatList\",\"type\":[{\"type\":\"array\",\"items\":\"float\"},\"null\"]},{\"name\":\"aIntegerMap\",\"type\":[{\"type\":\"map\",\"values\":\"int\"},\"null\"]},{\"name\":\"aLongMap\",\"type\":[{\"type\":\"map\",\"values\":\"long\"},\"null\"]},{\"name\":\"aDoubleMap\",\"type\":[{\"type\":\"map\",\"values\":\"double\"},\"null\"]},{\"name\":\"aBigDecimalMap\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},\"null\"]},{\"name\":\"aUuidSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},\"null\"]},{\"name\":\"aDateSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"int\",\"logicalType\":\"date\"}},\"null\"]},{\"name\":\"aTimestampSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},\"null\"]}]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = AvroSchemaLoader.parseClass(PrimitiveContainerClass.class);
        assertEquals(PrimitiveContainerClass.class, record.getClazz());
        assertEquals("PrimitiveContainerClass", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases", record.getNamespace());
        assertEquals(11, record.getFields().size());
        for (AvroField field : record.getFields()) {
            checkField(field);
        }
    }

    private void checkField(AvroField field) {
        String fieldName = field.getName();
        switch (fieldName) {
            case "aStringList": {
                assertEquals("java.util.List", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(List.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.STRING, field.getAvroType().getItemType().getSchemaType());
                assertEquals(null, field.getAvroType().getValueType());
                assertEquals(19, field.getAvroType().getPrecision());
                assertEquals(2, field.getAvroType().getScale());
            }
        }
    }
}
