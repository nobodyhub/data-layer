package com.nobodyhub.datalayer.core;

import org.apache.avro.LogicalTypes;
import org.junit.Test;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-19.
 */
public class AvroSchemaConverterParseLogcalTypeTest {
    @Column
    private static BigDecimal FIELD_BIGDECIMAL;
    @Column(precision = 10)
    private static BigDecimal FIELD_BIGDECIMAL_PRECISION;
    @Column(scale = 9)
    private static BigDecimal FIELD_BIGDECIMAL_SCALE;
    @Column(precision = 2, scale = 1)
    private static BigDecimal FIELD_BIGDECIMAL_BOTH;
    private static UUID FIELD_UUID;
    private static Date FIELD_DATE;
    private static Timestamp FIELD_TIMESTAMP;

    @Test
    public void test() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            AvroField avroField = new AvroField(field.getType());
            avroField.setField(field);
            AvroSchemaConverter.parseLogicalType(field.getGenericType(), avroField);
            doAssert(field.getGenericType(), avroField);
        }
    }

    protected void doAssert(Type type, AvroField avroField) {
        if (!(type instanceof Class)) {
            return;
        }
        Class cls = (Class) type;
        if (BigDecimal.class == cls) {
            String fieldName = avroField.getField().getName();
            if ("FIELD_BIGDECIMAL".equals(fieldName)) {
                assertEquals(0, ((LogicalTypes.Decimal) avroField.getLogicalType()).getPrecision());
                assertEquals(0, ((LogicalTypes.Decimal) avroField.getLogicalType()).getScale());
            } else if ("FIELD_BIGDECIMAL_PRECISION".equals(fieldName)) {
                assertEquals(10, ((LogicalTypes.Decimal) avroField.getLogicalType()).getPrecision());
                assertEquals(0, ((LogicalTypes.Decimal) avroField.getLogicalType()).getScale());
            } else if ("FIELD_BIGDECIMAL_SCALE".equals(fieldName)) {
                assertEquals(0, ((LogicalTypes.Decimal) avroField.getLogicalType()).getPrecision());
                assertEquals(9, ((LogicalTypes.Decimal) avroField.getLogicalType()).getScale());
            } else if ("FIELD_BIGDECIMAL_BOTH".equals(fieldName)) {
                assertEquals(2, ((LogicalTypes.Decimal) avroField.getLogicalType()).getPrecision());
                assertEquals(1, ((LogicalTypes.Decimal) avroField.getLogicalType()).getScale());
            }
        } else if (UUID.class == cls) {
            assertEquals(LogicalTypes.uuid(), avroField.getLogicalType());
        } else if (Date.class == cls) {
            assertEquals(LogicalTypes.date(), avroField.getLogicalType());
        } else if (Timestamp.class == cls) {
            assertEquals(LogicalTypes.timestampMillis(), avroField.getLogicalType());
        }
    }

}