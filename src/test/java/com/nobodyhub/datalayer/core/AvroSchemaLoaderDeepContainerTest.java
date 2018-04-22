package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.ComplexClass;
import com.nobodyhub.datalayer.core.cases.PrimitiveClass;
import com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass;
import com.nobodyhub.datalayer.core.cases.SimpleEnum;
import com.nobodyhub.datalayer.core.cases.within.ComplexEnum;
import com.nobodyhub.datalayer.core.cases.within.DeepContainer;
import com.nobodyhub.datalayer.core.cases.within.InheritedClass;
import org.apache.avro.LogicalTypes;
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
public class AvroSchemaLoaderDeepContainerTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() throws ClassNotFoundException {
        avroSchemaLoader.load(
                DeepContainer.class,
                ComplexClass.class,
                InheritedClass.class,
                SimpleEnum.class,
                PrimitiveClass.class,
                PrimitiveContainerClass.class,
                ComplexEnum.class);
        assertEquals("{\"type\":\"record\",\"name\":\"DeepContainer\",\"namespace\":\"com.nobodyhub.datalayer.core.cases.within\",\"fields\":[{\"name\":\"aSimpleEnumMapList\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":{\"type\":\"enum\",\"name\":\"SimpleEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"symbols\":[\"S1\",\"S2\",\"S3\"]}}},\"null\"]},{\"name\":\"aBigDecimalSetListMap\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}}}},\"null\"]},{\"name\":\"aComplexClassListSetMapMapMap\",\"type\":{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":{\"type\":\"record\",\"name\":\"ComplexClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"primitiveClass\",\"type\":{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"fields\":[{\"name\":\"aString\",\"type\":[\"string\",\"null\"]},{\"name\":\"aByteBuffer\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"int\",\"null\"]},{\"name\":\"along\",\"type\":[\"long\",\"null\"]},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"afloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"aFloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aboolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimeStamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}},{\"name\":\"primitiveContainerClass\",\"type\":{\"type\":\"record\",\"name\":\"PrimitiveContainerClass\",\"fields\":[{\"name\":\"aStringList\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"aByteBufferList\",\"type\":{\"type\":\"array\",\"items\":\"bytes\"}},{\"name\":\"aBooleanList\",\"type\":[{\"type\":\"array\",\"items\":\"boolean\"},\"null\"]},{\"name\":\"aFloatList\",\"type\":[{\"type\":\"array\",\"items\":\"float\"},\"null\"]},{\"name\":\"aIntegerMap\",\"type\":[{\"type\":\"map\",\"values\":\"int\"},\"null\"]},{\"name\":\"aLongMap\",\"type\":[{\"type\":\"map\",\"values\":\"long\"},\"null\"]},{\"name\":\"aDoubleMap\",\"type\":[{\"type\":\"map\",\"values\":\"double\"},\"null\"]},{\"name\":\"aBigDecimalMap\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},\"null\"]},{\"name\":\"aUuidSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},\"null\"]},{\"name\":\"aDateSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"int\",\"logicalType\":\"date\"}},\"null\"]},{\"name\":\"aTimestampSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},\"null\"]}]}},{\"name\":\"simpleEnum\",\"type\":\"SimpleEnum\"},{\"name\":\"complexEnum\",\"type\":{\"type\":\"enum\",\"name\":\"ComplexEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.cases.within\",\"symbols\":[\"C1\",\"C2\",\"C3\"]}}]}}}}}}},{\"name\":\"aInheritedClassMapListMapMapSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":{\"type\":\"record\",\"name\":\"InheritedClass\",\"fields\":[{\"name\":\"anotherString\",\"type\":[\"string\",\"null\"]},{\"name\":\"simpleEnum\",\"type\":\"com.nobodyhub.datalayer.core.cases.SimpleEnum\"},{\"name\":\"primitiveClassList\",\"type\":{\"type\":\"array\",\"items\":\"com.nobodyhub.datalayer.core.cases.PrimitiveClass\"}},{\"name\":\"aString\",\"type\":[\"string\",\"null\"]},{\"name\":\"aByteBuffer\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"int\",\"null\"]},{\"name\":\"along\",\"type\":[\"long\",\"null\"]},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"afloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"aFloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aboolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimeStamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}}}}}},\"null\"]}]}",
                avroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.within.DeepContainer").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = avroSchemaLoader.parseClass(DeepContainer.class);
        assertEquals(DeepContainer.class, record.getClazz());
        assertEquals("DeepContainer", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.within.DeepContainer", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases.within", record.getNamespace());
        assertEquals(4, record.getFields().size());
        for (AvroField field : record.getFields()) {
            checkField(field);
        }
    }

    private void checkField(AvroField field) {
        String fieldName = field.getName();
        switch (fieldName) {
            case "aSimpleEnumMapList": {
                assertEquals("java.util.List", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(List.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getItemType().getSchemaType());
                assertEquals(Schema.Type.ENUM, field.getAvroType().getItemType().getValueType().getSchemaType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aBigDecimalSetListMap": {
                assertEquals("java.util.Map", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Map.class, field.getAvroType().getType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getValueType().getSchemaType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getValueType().getItemType().getSchemaType());
                assertEquals(LogicalTypes.decimal(19, 2), field.getAvroType().getValueType().getItemType().getItemType().getLogicalType());
                break;
            }
            case "aComplexClassListSetMapMapMap": {
                assertEquals("java.util.Map", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(Map.class, field.getAvroType().getType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getValueType().getSchemaType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getValueType().getValueType().getSchemaType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getValueType().getValueType().getValueType().getSchemaType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getValueType().getValueType().getValueType().getItemType().getSchemaType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getValueType().getValueType().getValueType().getItemType().getItemType().getSchemaType());
                break;
            }
            case "aInheritedClassMapListMapMapSet": {
                assertEquals("java.util.Set", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Set.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getItemType().getSchemaType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getItemType().getValueType().getSchemaType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getItemType().getValueType().getValueType().getSchemaType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getItemType().getValueType().getValueType().getItemType().getSchemaType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getItemType().getValueType().getValueType().getItemType().getValueType().getSchemaType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            default: {
                assertTrue("Should not come here!", false);
            }
        }
    }
}