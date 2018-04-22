package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.ComplexClass;
import com.nobodyhub.datalayer.core.cases.PrimitiveClass;
import com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass;
import com.nobodyhub.datalayer.core.cases.SimpleEnum;
import com.nobodyhub.datalayer.core.cases.within.ComplexEnum;
import org.apache.avro.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan
 */
public class AvorSchemaLoaderComplexClassTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() throws ClassNotFoundException {
        avroSchemaLoader.load(
                ComplexClass.class,
                PrimitiveClass.class,
                PrimitiveContainerClass.class,
                SimpleEnum.class,
                ComplexEnum.class);
        assertEquals("{\"type\":\"record\",\"name\":\"ComplexClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"primitiveClass\",\"type\":{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"fields\":[{\"name\":\"aString\",\"type\":[\"string\",\"null\"]},{\"name\":\"aByteBuffer\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"int\",\"null\"]},{\"name\":\"along\",\"type\":[\"long\",\"null\"]},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"afloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"aFloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aboolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimeStamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}},{\"name\":\"primitiveContainerClass\",\"type\":{\"type\":\"record\",\"name\":\"PrimitiveContainerClass\",\"fields\":[{\"name\":\"aStringList\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"aByteBufferList\",\"type\":{\"type\":\"array\",\"items\":\"bytes\"}},{\"name\":\"aBooleanList\",\"type\":[{\"type\":\"array\",\"items\":\"boolean\"},\"null\"]},{\"name\":\"aFloatList\",\"type\":[{\"type\":\"array\",\"items\":\"float\"},\"null\"]},{\"name\":\"aIntegerMap\",\"type\":[{\"type\":\"map\",\"values\":\"int\"},\"null\"]},{\"name\":\"aLongMap\",\"type\":[{\"type\":\"map\",\"values\":\"long\"},\"null\"]},{\"name\":\"aDoubleMap\",\"type\":[{\"type\":\"map\",\"values\":\"double\"},\"null\"]},{\"name\":\"aBigDecimalMap\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},\"null\"]},{\"name\":\"aUuidSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},\"null\"]},{\"name\":\"aDateSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"int\",\"logicalType\":\"date\"}},\"null\"]},{\"name\":\"aTimestampSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},\"null\"]}]}},{\"name\":\"simpleEnum\",\"type\":{\"type\":\"enum\",\"name\":\"SimpleEnum\",\"symbols\":[\"S1\",\"S2\",\"S3\"]}},{\"name\":\"complexEnum\",\"type\":{\"type\":\"enum\",\"name\":\"ComplexEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.cases.within\",\"symbols\":[\"C1\",\"C2\",\"C3\"]}}]}",
                avroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.ComplexClass").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = avroSchemaLoader.parseClass(ComplexClass.class);
        assertEquals(ComplexClass.class, record.getClazz());
        assertEquals("ComplexClass", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.ComplexClass", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases", record.getNamespace());
        assertEquals(4, record.getFields().size());
        for (AvroField field : record.getFields()) {
            checkField(field);
        }
    }

    private void checkField(AvroField field) {
        String fieldName = field.getName();
        switch (fieldName) {
            case "primitiveClass": {
                assertEquals("com.nobodyhub.datalayer.core.cases.PrimitiveClass", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(PrimitiveClass.class, field.getAvroType().getType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "primitiveContainerClass": {
                assertEquals("com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(PrimitiveContainerClass.class, field.getAvroType().getType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "simpleEnum": {
                assertEquals("com.nobodyhub.datalayer.core.cases.SimpleEnum", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(SimpleEnum.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ENUM, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "complexEnum": {
                assertEquals("com.nobodyhub.datalayer.core.cases.within.ComplexEnum", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(ComplexEnum.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ENUM, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            default: {
                assertTrue("Should not come here!", false);
            }
        }
    }
}