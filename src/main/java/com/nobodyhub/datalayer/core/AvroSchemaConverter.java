package com.nobodyhub.datalayer.core;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nobodyhub.datalayer.core.exception.AvroCoreException;
import org.apache.avro.AvroTypeException;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.specific.SpecificData;

import javax.annotation.Nullable;
import javax.persistence.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Convert a Hibernate entity to avro schema file
 * <p>
 * Reference:
 * - {@link ReflectData#createSchema(Type, Map)}
 * <p>
 * TODO: scan all classed in current classloader/specified package
 *
 * @author Ryan
 */
public final class AvroSchemaConverter {
    public static final Map<String, Schema> schemas = Maps.newHashMap();
    public static final Map<String, AvroRecord> records = Maps.newHashMap();
    ;

    public static Schema getSchema(String name) {
        Schema schema = schemas.get(name);
        if (schema == null) {
            AvroRecord record = records.get(name);
            if (record == null) {
                throw new AvroCoreException(String.format("No AvroRecord Found for name: '%s'", name));
            }
            schema = record.toSchema();
            schemas.put(schema.getFullName(), schema);
        }
        return schema;
    }

    private AvroSchemaConverter() {
    }

    public static Schema parse(Class<?>... classes) {


        for (Class<?> clazz : classes) {
            records.put(clazz.getName(), parseClass(clazz));
        }

        //TODO
        return null;
    }

    protected static AvroRecord parseClass(Class<?> clazz) {
        AvroRecord record = new AvroRecord(clazz);
        fillFieldInfo(record);
        return record;
    }

    protected static void fillFieldInfo(AvroRecord record) {
        Class<?> clz = record.getClazz();
        while (clz != null) {
            for (Field field : clz.getDeclaredFields()) {
                Annotation[] annotations = field.getAnnotationsByType(Column.class);
                if (annotations != null && annotations.length > 0) {
                    AvroField avroField = new AvroField(field.getType());
                    avroField.setField(field);
                    parseType(field.getGenericType(), avroField);
                }
            }
            clz = clz.getSuperclass();
        }
    }

    public static void parseType(Type type, AvroField avroField) {
        parseBasicType(type, avroField);
        parseLogicalType(type, avroField);
    }

    /**
     * Parse basic types from Java type
     *
     * <b>Note:</b>
     * 1. Because the limitation of Avro(as of 1.8.2), the key of map can only be String
     * while the {@link javax.persistence.MapKey} allows more than String type
     * 2. {@link Schema.Type#UNION} and {@link Schema.Type#FIXED} have NO corresponding types in Java
     *
     * @param type
     * @param avroField
     * @return the Type for {@link Schema}
     * @see SpecificData#createSchema
     */
    protected static void parseBasicType(Type type, AvroField avroField) {
        if (type instanceof Class
                && CharSequence.class.isAssignableFrom((Class) type)) {
            avroField.setType(Schema.Type.STRING);
        } else if (type == ByteBuffer.class) {
            avroField.setType(Schema.Type.BYTES);
        } else if ((type == Integer.class) || (type == Integer.TYPE)) {
            avroField.setType(Schema.Type.INT);
        } else if ((type == Long.class) || (type == Long.TYPE)) {
            avroField.setType(Schema.Type.LONG);
        } else if ((type == Float.class) || (type == Float.TYPE)) {
            avroField.setType(Schema.Type.FLOAT);
        } else if ((type == Double.class) || (type == Double.TYPE)) {
            avroField.setType(Schema.Type.DOUBLE);
        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {
            avroField.setType(Schema.Type.BOOLEAN);
        } else if ((type == Void.class) || (type == Void.TYPE)) {
            avroField.setType(Schema.Type.NULL);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) (Type) type;
            Class raw = (Class) ptype.getRawType();
            java.lang.reflect.Type[] params = ptype.getActualTypeArguments();
            if (Collection.class.isAssignableFrom(raw)) {
                // array
                if (params.length != 1) {
                    throw new AvroTypeException("No array type specified.");
                }
                avroField.setType(Schema.Type.ARRAY);
                AvroField itemType = new AvroField(params[0].getClass());
                parseType(params[0], itemType);
                avroField.setItemType(itemType);
            } else if (Map.class.isAssignableFrom(raw)) {
                // map
                java.lang.reflect.Type key = params[0];
                java.lang.reflect.Type value = params[1];
                if (!(key instanceof Class
                        && CharSequence.class.isAssignableFrom((Class) key))) {
                    throw new AvroTypeException("Map key class not CharSequence: " + key);
                }
                avroField.setType(Schema.Type.MAP);
                AvroField valueType = new AvroField(params[1].getClass());
                parseType(params[1], valueType);
                avroField.setValueType(valueType);
            } else {
                avroField.setType(Schema.Type.RECORD);
            }
        } else if (type instanceof Class) {
            Class clazz = (Class) type;
            if (clazz.isEnum()) {
                //enum
                avroField.setType(Schema.Type.ENUM);
                avroField.setSymbols(Sets.newHashSet(Arrays.stream(clazz.getDeclaredFields()).map(new Function<Field, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable Field input) {
                        return input.getName();
                    }
                }).collect(Collectors.toList())));
            } else {
                // class
                avroField.setType(Schema.Type.RECORD);
            }
        }
    }

    /**
     * Parse logical types from Java type
     *
     * @param type
     * @param avroField
     * @see <a href="https://avro.apache.org/docs/1.8.1/spec.html#Logical+Types">Logical Types</a>
     */
    protected static void parseLogicalType(Type type, AvroField avroField) {
        if (!(type instanceof Class)) {
            return;
        }
        Class cls = (Class) type;
        Schema schema = null;
        if (BigDecimal.class == cls) {
            Field field = avroField.getField();
            /**
             * precision/scale initial value follows {@link Column}
             */
            int precision = 0, scale = 0;
            if (field != null) {
                Column annotation = field.getAnnotation(Column.class);
                if (annotation != null) {
                    precision = annotation.precision();
                    scale = annotation.scale();
                }
            }
            LogicalType decimal = LogicalTypes.decimal(precision, scale);
            //add to avroField
            avroField.setLogicalType(decimal);
            //add to schemas
            schema = decimal.addToSchema(Schema.create(Schema.Type.STRING));
        } else if (UUID.class == cls) {
            LogicalType uuid = LogicalTypes.uuid();
            avroField.setLogicalType(uuid);
            schema = uuid.addToSchema(Schema.create(Schema.Type.STRING));
        } else if (Date.class == cls) {
            LogicalType date = LogicalTypes.date();
            avroField.setLogicalType(date);
            schema = date.addToSchema(Schema.create(Schema.Type.INT));
        } else if (Timestamp.class == cls) {
            LogicalType timestamp = LogicalTypes.timestampMillis();
            avroField.setLogicalType(timestamp);
            schema = timestamp.addToSchema(Schema.create(Schema.Type.LONG));
        }
        if (schema != null) {
            schemas.put(schema.getFullName(), schema);
        }
    }
}
