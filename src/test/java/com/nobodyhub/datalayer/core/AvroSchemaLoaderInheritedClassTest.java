package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.AvroField;
import com.nobodyhub.datalayer.core.AvroRecord;
import com.nobodyhub.datalayer.core.AvroSchemaLoader;
import com.nobodyhub.datalayer.core.cases.*;
import org.apache.avro.Schema;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderInheritedClassTest extends AvroSchemaLoaderPrimitiveClassTest {
    @Test
    public void testLoad() {
        AvroSchemaLoader.load(
                InheritedClass.class,
                PrimitiveClass.class,
                SimpleEnum.class);
        assertEquals("{\"type\":\"record\",\"name\":\"InheritedClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"anotherString\",\"type\":[\"string\",\"null\"]},{\"name\":\"simpleEnum\",\"type\":{\"type\":\"enum\",\"name\":\"SimpleEnum\",\"symbols\":[\"S1\",\"S2\",\"S3\"]}},{\"name\":\"primitiveClassList\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"fields\":[{\"name\":\"aString\",\"type\":[\"string\",\"null\"]},{\"name\":\"aByteBuffer\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"int\",\"null\"]},{\"name\":\"along\",\"type\":[\"long\",\"null\"]},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"afloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"aFloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aboolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":2}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimeStamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}}},{\"name\":\"aString\",\"type\":[\"string\",\"null\"]},{\"name\":\"aByteBuffer\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"int\",\"null\"]},{\"name\":\"along\",\"type\":[\"long\",\"null\"]},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"afloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"aFloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aboolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":2}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimeStamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.InheritedClass").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = AvroSchemaLoader.parseClass(InheritedClass.class);
        assertEquals(InheritedClass.class, record.getClazz());
        assertEquals("InheritedClass", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.InheritedClass", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases", record.getNamespace());
        assertEquals(19, record.getFields().size());
        for (AvroField field : record.getFields()) {
            checkField(field);
        }
    }

    protected void checkField(AvroField field) {
        String fieldName = field.getName();
        switch (fieldName) {
            case "anotherString": {
                assertEquals("java.lang.String", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(String.class, field.getAvroType().getType());
                assertEquals(Schema.Type.STRING, field.getAvroType().getSchemaType());
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
            case "primitiveClassList": {
                assertEquals("java.util.List", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(List.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getItemType().getSchemaType());
                assertEquals("com.nobodyhub.datalayer.core.cases.PrimitiveClass", field.getAvroType().getItemType().getQualifiedName());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            default: {
                super.checkField(field);
            }
        }
    }
}