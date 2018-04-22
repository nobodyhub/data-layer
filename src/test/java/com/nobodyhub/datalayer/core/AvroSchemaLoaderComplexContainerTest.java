package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.*;
import org.apache.avro.Schema;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderComplexContainerTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() throws ClassNotFoundException {
        AvroSchemaLoader.load(ComplexContainer.class,
                ComplexClass.class,
                PrimitiveClass.class,
                PrimitiveContainerClass.class,
                SimpleEnum.class,
                ComplexEnum.class
        );
        assertEquals("{\"type\":\"record\",\"name\":\"ComplexContainer\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"complexClassList\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ComplexClass\",\"fields\":[{\"name\":\"primitiveClass\",\"type\":{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"fields\":[{\"name\":\"aString\",\"type\":[\"string\",\"null\"]},{\"name\":\"aByteBuffer\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"int\",\"null\"]},{\"name\":\"along\",\"type\":[\"long\",\"null\"]},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"afloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"aFloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aboolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimeStamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}},{\"name\":\"primitiveContainerClass\",\"type\":{\"type\":\"record\",\"name\":\"PrimitiveContainerClass\",\"fields\":[{\"name\":\"aStringList\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"aByteBufferList\",\"type\":{\"type\":\"array\",\"items\":\"bytes\"}},{\"name\":\"aBooleanList\",\"type\":[{\"type\":\"array\",\"items\":\"boolean\"},\"null\"]},{\"name\":\"aFloatList\",\"type\":[{\"type\":\"array\",\"items\":\"float\"},\"null\"]},{\"name\":\"aIntegerMap\",\"type\":[{\"type\":\"map\",\"values\":\"int\"},\"null\"]},{\"name\":\"aLongMap\",\"type\":[{\"type\":\"map\",\"values\":\"long\"},\"null\"]},{\"name\":\"aDoubleMap\",\"type\":[{\"type\":\"map\",\"values\":\"double\"},\"null\"]},{\"name\":\"aBigDecimalMap\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},\"null\"]},{\"name\":\"aUuidSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},\"null\"]},{\"name\":\"aDateSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"int\",\"logicalType\":\"date\"}},\"null\"]},{\"name\":\"aTimestampSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},\"null\"]}]}},{\"name\":\"simpleEnum\",\"type\":{\"type\":\"enum\",\"name\":\"SimpleEnum\",\"symbols\":[\"S1\",\"S2\",\"S3\"]}},{\"name\":\"complexEnum\",\"type\":{\"type\":\"enum\",\"name\":\"ComplexEnum\",\"symbols\":[\"C1\",\"C2\",\"C3\"]}}]}},\"null\"]},{\"name\":\"complexClassMap\",\"type\":{\"type\":\"map\",\"values\":\"ComplexClass\"}},{\"name\":\"complexClassSet\",\"type\":[{\"type\":\"array\",\"items\":\"ComplexClass\"},\"null\"]}]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.ComplexContainer").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = AvroSchemaLoader.parseClass(ComplexContainer.class);
        assertEquals(ComplexContainer.class, record.getClazz());
        assertEquals("ComplexContainer", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.ComplexContainer", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases", record.getNamespace());
        assertEquals(3, record.getFields().size());
        for (AvroField field : record.getFields()) {
            checkField(field);
        }
    }

    private void checkField(AvroField field) {
        String fieldName = field.getName();
        switch (fieldName) {
            case "complexClassList": {
                assertEquals("java.util.List", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(List.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getItemType().getSchemaType());
                assertEquals("com.nobodyhub.datalayer.core.cases.ComplexClass", field.getAvroType().getItemType().getQualifiedName());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "complexClassMap": {
                assertEquals("java.util.Map", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(Map.class, field.getAvroType().getType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getValueType().getSchemaType());
                assertEquals("com.nobodyhub.datalayer.core.cases.ComplexClass", field.getAvroType().getValueType().getQualifiedName());
                break;
            }
            case "complexClassSet": {
                assertEquals("java.util.Set", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Set.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getItemType().getSchemaType());
                assertEquals("com.nobodyhub.datalayer.core.cases.ComplexClass", field.getAvroType().getItemType().getQualifiedName());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            default: {
                assertTrue("Should not come here!", false);
            }
        }
    }
}