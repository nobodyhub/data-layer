package com.nobodyhub.datalayer.core;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.junit.Test;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author yan_h
 * @since 2018-04-19.
 */
public class AvroSchemaConverterParseBasicTypeTest {

    @Column
    private static String FIELD_STRING;
    @Column
    private static ByteBuffer FIELD_BYTES;
    @Column
    private static int FIELD_INT;
    @Column
    private static Integer FIELD_INT1;
    @Column
    private static long FIELD_LONG;
    @Column
    private static Long FIELD_LONG1;
    @Column
    private static Float FIELD_FLOAT;
    @Column
    private static float FIELD_FLOAT1;
    @Column
    private static Double FIELD_DOUBLE;
    @Column
    private static double FIELD_DOUBLE1;
    @Column
    private static Boolean FIELD_BOOLEAN;
    @Column
    private static boolean FIELD_BOOLEAN1;
    @Column
    private static Void FIELD_VOID;
    @Column
    private static List<String> FIELD_LIST;
    @Column
    private static ArrayList<String> FIELD_ARRAYLIST;
    @Column
    private static LinkedList<String> FIELD_LINKEDLIST;
    @Column
    private static List<Schema.Type> FIELD_ENUM_LIST;
    @Column
    private static Set<Timestamp> FIELD_SET;
    @Column
    private static HashSet<BigDecimal> FIELD_HASHSET;
    @Column
    private static TreeSet<java.sql.Date> FIELD_TREESET;
    @Column
    private static Map<String, Integer> FIELD_MAP;
    @Column
    private static HashMap<String, BigDecimal> FIELD_HASHMAP;
    @Column
    private static TreeMap<String, UUID> FIELD_TREEMAP;
    @Column
    private static Map<String, Schema.Type> FIELD_ENUM_MAP;
    @Column
    private static AvroSchemaConverterParseBasicTypeTest FIELD_RECORD;
    @Column
    private static TypeClass<String> FIELD_TYPECLASS;
    @Column
    private static Schema.Type FIELD_ENUM;


    @Test
    public void test() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            AvroType avroType = new AvroType(field.getType());
            avroType.setField(field);
            AvroSchemaConverter.parseBasicType(field.getGenericType(), avroType);
            doAssert(field.getGenericType(), avroType);
        }
    }

    protected void doAssert(Type type, AvroType avroType) {
        String fieldName = avroType.getField().getName();
        if (type instanceof Class
                && CharSequence.class.isAssignableFrom((Class) type)) {
            assertEquals(Schema.Type.STRING, avroType.getSchemaType());
        } else if (type == ByteBuffer.class) {
            assertEquals(Schema.Type.BYTES, avroType.getSchemaType());
        } else if ((type == Integer.class) || (type == Integer.TYPE)) {
            assertEquals(Schema.Type.INT, avroType.getSchemaType());
        } else if ((type == Long.class) || (type == Long.TYPE)) {
            assertEquals(Schema.Type.LONG, avroType.getSchemaType());
        } else if ((type == Float.class) || (type == Float.TYPE)) {
            assertEquals(Schema.Type.FLOAT, avroType.getSchemaType());
        } else if ((type == Double.class) || (type == Double.TYPE)) {
            assertEquals(Schema.Type.DOUBLE, avroType.getSchemaType());
        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {
            assertEquals(Schema.Type.BOOLEAN, avroType.getSchemaType());
        } else if ((type == Void.class) || (type == Void.TYPE)) {
            assertEquals(Schema.Type.NULL, avroType.getSchemaType());
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) (Type) type;
            Class raw = (Class) ptype.getRawType();
            if (Collection.class.isAssignableFrom(raw)) {
                assertEquals(Schema.Type.ARRAY, avroType.getSchemaType());
                if ("FIELD_SET".equals(fieldName)) {
                    assertEquals(LogicalTypes.timestampMillis(), avroType.getItemType().getLogicalType());
                } else if ("FIELD_TREESET".equals(fieldName)) {
                    assertEquals(LogicalTypes.date(), avroType.getItemType().getLogicalType());
                } else if ("FIELD_ENUM_LIST".equals(fieldName)) {
                    assertEquals(Schema.Type.ENUM, avroType.getItemType().getSchemaType());
                } else if ("FIELD_HASHSET".equals(fieldName)) {
                    assertEquals(LogicalTypes.decimal(19, 2), avroType.getItemType().getLogicalType());
                } else if ("FIELD_LIST".equals(fieldName)
                        || "FIELD_ARRAYLIST".equals(fieldName)
                        || "FIELD_LINKEDLIST".equals(fieldName)) {
                    assertEquals(Schema.Type.STRING, avroType.getItemType().getSchemaType());
                } else {
                    //should not reach here
                    assertTrue(false);
                }
            } else if (Map.class.isAssignableFrom(raw)) {
                assertEquals(Schema.Type.MAP, avroType.getSchemaType());
                if ("FIELD_MAP".equals(fieldName)) {
                    assertEquals(Schema.Type.INT, avroType.getValueType().getSchemaType());
                } else if ("FIELD_HASHMAP".equals(fieldName)) {
                    assertEquals(LogicalTypes.decimal(19, 2), avroType.getValueType().getLogicalType());
                } else if ("FIELD_TREEMAP".equals(fieldName)) {
                    assertEquals(LogicalTypes.uuid(), avroType.getValueType().getLogicalType());
                } else if ("FIELD_ENUM_MAP".equals(fieldName)) {
                    assertEquals(Schema.Type.ENUM, avroType.getValueType().getSchemaType());
                } else {
                    //should not reach here
                    assertTrue(false);
                }
            } else {
                assertEquals(Schema.Type.RECORD, avroType.getSchemaType());
            }
        } else if (type instanceof Class) {
            Class clazz = (Class) type;
            if (clazz.isEnum()) {
                //enum
                if ("FIELD_ENUM".equals(fieldName)) {
                    assertEquals(Schema.Type.ENUM, avroType.getSchemaType());
                } else {
                    //should not reach here
                    assertTrue(false);
                }
            } else {
                // class
                assertEquals(Schema.Type.RECORD, avroType.getSchemaType());
            }
        }
    }

    private static class TypeClass<T> {

    }
}