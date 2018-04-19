package com.nobodyhub.datalayer.core;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018-04-19.
 */
public class AvroSchemaConverterParseBasicTypeTest {
    private static String FIELD_STRING;
    private static ByteBuffer FIELD_BYTES;
    private static int FIELD_INT;
    private static Integer FIELD_INT1;
    private static long FIELD_LONG;
    private static Long FIELD_LONG1;
    private static Float FIELD_FLOAT;
    private static float FIELD_FLOAT1;
    private static Double FIELD_DOUBLE;
    private static double FIELD_DOUBLE1;
    private static Boolean FIELD_BOOLEAN;
    private static boolean FIELD_BOOLEAN1;
    private static Void FIELD_VOID;
    private static List<String> FIELD_LIST;
    private static ArrayList<String> FIELD_ARRAYLIST;
    private static LinkedList<String> FIELD_LINKEDLIST;
    private static Set<Timestamp> FIELD_SET;
    private static HashSet<String> FIELD_HASHSET;
    private static TreeSet<java.sql.Date> FIELD_TREESET;
    private static Map<String, Integer> FIELD_MAP;
    private static HashMap<String, BigDecimal> FIELD_HASHMAP;
    private static TreeMap<String, UUID> FIELD_TREEMAP;
    private static AvroSchemaConverterParseBasicTypeTest FIELD_RECORD;
    private static TypeClass<String> FIELD_TYPECLASS;
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
        if (type instanceof Class
                && CharSequence.class.isAssignableFrom((Class) type)) {
            assertEquals(Schema.Type.STRING, avroType.getType());
        } else if (type == ByteBuffer.class) {
            assertEquals(Schema.Type.BYTES, avroType.getType());
        } else if ((type == Integer.class) || (type == Integer.TYPE)) {
            assertEquals(Schema.Type.INT, avroType.getType());
        } else if ((type == Long.class) || (type == Long.TYPE)) {
            assertEquals(Schema.Type.LONG, avroType.getType());
        } else if ((type == Float.class) || (type == Float.TYPE)) {
            assertEquals(Schema.Type.FLOAT, avroType.getType());
        } else if ((type == Double.class) || (type == Double.TYPE)) {
            assertEquals(Schema.Type.DOUBLE, avroType.getType());
        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {
            assertEquals(Schema.Type.BOOLEAN, avroType.getType());
        } else if ((type == Void.class) || (type == Void.TYPE)) {
            assertEquals(Schema.Type.NULL, avroType.getType());
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) (Type) type;
            Class raw = (Class) ptype.getRawType();
            String fieldName = avroType.getField().getName();
            if (Collection.class.isAssignableFrom(raw)) {
                assertEquals(Schema.Type.ARRAY, avroType.getType());
                if ("FIELD_SET".equals(fieldName)) {
                    assertEquals(LogicalTypes.timestampMillis(), avroType.getItemType().getLogicalType());
                } else if ("FIELD_TREESET".equals(fieldName)) {
                    assertEquals(LogicalTypes.date(), avroType.getItemType().getLogicalType());
                } else {
                    assertEquals(Schema.Type.STRING, avroType.getItemType().getType());
                }
            } else if (Map.class.isAssignableFrom(raw)) {
                assertEquals(Schema.Type.MAP, avroType.getType());
                if ("FIELD_MAP".equals(fieldName)) {
                    assertEquals(Schema.Type.INT, avroType.getValueType().getType());
                } else if ("FIELD_HASHMAP".equals(fieldName)) {
                    assertEquals(LogicalTypes.decimal(19, 2), avroType.getValueType().getLogicalType());
                } else if ("FIELD_TREEMAP".equals(fieldName)) {
                    assertEquals(LogicalTypes.uuid(), avroType.getValueType().getLogicalType());
                } else {
                    assertEquals(Schema.Type.INT, avroType.getValueType().getType());
                }
            } else {
                assertEquals(Schema.Type.RECORD, avroType.getType());
            }
        } else if (type instanceof Class) {
            Class clazz = (Class) type;
            if (clazz.isEnum()) {
                //enum
                assertEquals(Schema.Type.ENUM, avroType.getType());
            } else {
                // class
                assertEquals(Schema.Type.RECORD, avroType.getType());
            }
        }
    }

    static class TypeClass<T> {

    }
}