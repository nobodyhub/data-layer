package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.PrimitiveClass;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author yan_h
 * @since 2018-04-20.
 */
public class AvroSchemaLoaderPrimitiveClassTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() {
        AvroSchemaLoader.load(PrimitiveClass.class);
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"fields\":[{\"name\":\"aString\",\"type\":[\"string\",\"null\"]},{\"name\":\"aByteBuffer\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aInteger\",\"type\":[\"int\",\"null\"]},{\"name\":\"along\",\"type\":[\"long\",\"null\"]},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"afloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"aFloat\",\"type\":[\"float\",\"null\"]},{\"name\":\"adouble\",\"type\":\"double\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aboolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"aBoolean\",\"type\":\"boolean\"},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":2}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimeStamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}}]}",
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

    protected void checkField(AvroField field) {
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
                break;
            }
            case "aByteBuffer": {
                assertEquals("java.nio.ByteBuffer", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(ByteBuffer.class, field.getAvroType().getType());
                assertEquals(Schema.Type.BYTES, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aInt": {
                assertEquals("int", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(int.class, field.getAvroType().getType());
                assertEquals(Schema.Type.INT, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aInteger": {
                assertEquals("java.lang.Integer", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Integer.class, field.getAvroType().getType());
                assertEquals(Schema.Type.INT, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "along": {
                assertEquals("long", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(long.class, field.getAvroType().getType());
                assertEquals(Schema.Type.LONG, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aLong": {
                assertEquals("java.lang.Long", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Long.class, field.getAvroType().getType());
                assertEquals(Schema.Type.LONG, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "afloat": {
                assertEquals("float", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(float.class, field.getAvroType().getType());
                assertEquals(Schema.Type.FLOAT, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aFloat": {
                assertEquals("java.lang.Float", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Float.class, field.getAvroType().getType());
                assertEquals(Schema.Type.FLOAT, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "adouble": {
                assertEquals("double", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(double.class, field.getAvroType().getType());
                assertEquals(Schema.Type.DOUBLE, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aDouble": {
                assertEquals("java.lang.Double", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Double.class, field.getAvroType().getType());
                assertEquals(Schema.Type.DOUBLE, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aboolean": {
                assertEquals("boolean", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(boolean.class, field.getAvroType().getType());
                assertEquals(Schema.Type.BOOLEAN, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aBoolean": {
                assertEquals("java.lang.Boolean", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(Boolean.class, field.getAvroType().getType());
                assertEquals(Schema.Type.BOOLEAN, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aBigDecimal": {
                assertEquals("java.math.BigDecimal", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(BigDecimal.class, field.getAvroType().getType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getSchemaType());
                assertEquals(LogicalTypes.decimal(10, 2), field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aDate": {
                assertEquals("java.sql.Date", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(Date.class, field.getAvroType().getType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getSchemaType());
                assertEquals(LogicalTypes.date(), field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aTimeStamp": {
                assertEquals("java.sql.Timestamp", field.getQualifiedName());
                assertEquals(true, field.isNullable());
                assertEquals(Timestamp.class, field.getAvroType().getType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getSchemaType());
                assertEquals(LogicalTypes.timestampMillis(), field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            case "aUuid": {
                assertEquals("java.util.UUID", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(UUID.class, field.getAvroType().getType());
                assertEquals(Schema.Type.RECORD, field.getAvroType().getSchemaType());
                assertEquals(LogicalTypes.uuid(), field.getAvroType().getLogicalType());
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
