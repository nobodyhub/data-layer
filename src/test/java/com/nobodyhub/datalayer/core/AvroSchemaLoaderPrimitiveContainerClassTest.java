package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-20.
 */
public class AvroSchemaLoaderPrimitiveContainerClassTest {
    @Test
    public void testLoad() {
        AvroSchemaLoader.load(PrimitiveContainerClass.class);
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveContainerClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"aStringList\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"aByteBufferList\",\"type\":{\"type\":\"array\",\"items\":\"bytes\"}},{\"name\":\"aBooleanList\",\"type\":[{\"type\":\"array\",\"items\":\"boolean\"},\"null\"]},{\"name\":\"aFloatList\",\"type\":[{\"type\":\"array\",\"items\":\"float\"},\"null\"]},{\"name\":\"aIntegerMap\",\"type\":[{\"type\":\"map\",\"values\":\"int\"},\"null\"]},{\"name\":\"aLongMap\",\"type\":[{\"type\":\"map\",\"values\":\"long\"},\"null\"]},{\"name\":\"aDoubleMap\",\"type\":[{\"type\":\"map\",\"values\":\"double\"},\"null\"]},{\"name\":\"aBigDecimalMap\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":19,\"scale\":2}},\"null\"]},{\"name\":\"aUuidSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},\"null\"]},{\"name\":\"aDateSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"int\",\"logicalType\":\"date\"}},\"null\"]},{\"name\":\"aTimestampSet\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},\"null\"]}]}",
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
                break;
            }
            case "aByteBufferList": {
                assertEquals("java.util.List", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(List.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.BYTES, field.getAvroType().getItemType().getSchemaType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aBooleanList": {
                assertEquals("java.util.ArrayList", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(ArrayList.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.BOOLEAN, field.getAvroType().getItemType().getSchemaType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aFloatList": {
                assertEquals("java.util.LinkedList", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(LinkedList.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.FLOAT, field.getAvroType().getItemType().getSchemaType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aIntegerMap": {
                assertEquals("java.util.Map", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Map.class, field.getAvroType().getType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(Schema.Type.INT, field.getAvroType().getValueType().getSchemaType());
                break;
            }
            case "aLongMap": {
                assertEquals("java.util.Map", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Map.class, field.getAvroType().getType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(Schema.Type.LONG, field.getAvroType().getValueType().getSchemaType());
                break;
            }
            case "aDoubleMap": {
                assertEquals("java.util.HashMap", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(HashMap.class, field.getAvroType().getType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(Schema.Type.DOUBLE, field.getAvroType().getValueType().getSchemaType());
                break;
            }
            case "aBigDecimalMap": {
                assertEquals("java.util.TreeMap", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(TreeMap.class, field.getAvroType().getType());
                assertEquals(Schema.Type.MAP, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getValueType().getSchemaType());
                assertEquals(LogicalTypes.decimal(19, 2), field.getAvroType().getValueType().getLogicalType());
                break;
            }
            case "aUuidSet": {
                assertEquals("java.util.Set", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Set.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getItemType().getSchemaType());
                assertEquals(LogicalTypes.uuid(), field.getAvroType().getItemType().getLogicalType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aDateSet": {
                assertEquals("java.util.HashSet", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(HashSet.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getItemType().getSchemaType());
                assertEquals(LogicalTypes.date(), field.getAvroType().getItemType().getLogicalType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aTimestampSet": {
                assertEquals("java.util.TreeSet", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(TreeSet.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ARRAY, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getItemType().getSchemaType());
                assertEquals(LogicalTypes.timestampMillis(), field.getAvroType().getItemType().getLogicalType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
        }
    }
}
