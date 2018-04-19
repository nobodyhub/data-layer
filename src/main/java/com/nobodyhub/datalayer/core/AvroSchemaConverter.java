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

    private AvroSchemaConverter() {
    }

    public static Schema getSchema(String qualifiedName) {
        Schema schema = schemas.get(qualifiedName);
        if (schema == null) {
            AvroRecord record = records.get(qualifiedName);
            if (record == null) {
                throw new AvroCoreException(String.format("No AvroRecord Found for name: '%s'", qualifiedName));
            }
            schemas.put(qualifiedName, record.toSchema());
        }
        return schema;
    }

    public static Schema parse(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            AvroRecord record = parseClass(clazz);
            records.put(record.getQualifiedName(), record);
        }
        for(AvroRecord record: records.values()) {
            schemas.put(record.getQualifiedName(), record.toSchema());
        }
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
                    AvroField avroField = new AvroField(field);
                    parseType(field.getGenericType(), avroField.getAvroType());
                    record.addField(avroField);
                }
            }
            clz = clz.getSuperclass();
        }
    }

    public static void parseType(Type type, AvroType avroType) {
        parseBasicType(type, avroType);
        parseLogicalType(type, avroType);
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
     * @param avroType
     * @return the Type for {@link Schema}
     * @see SpecificData#createSchema
     */
    protected static void parseBasicType(Type type, AvroType avroType) {
        if (type instanceof Class
                && CharSequence.class.isAssignableFrom((Class) type)) {
            avroType.setType(Schema.Type.STRING);
        } else if (type == ByteBuffer.class) {
            avroType.setType(Schema.Type.BYTES);
        } else if ((type == Integer.class) || (type == Integer.TYPE)) {
            avroType.setType(Schema.Type.INT);
        } else if ((type == Long.class) || (type == Long.TYPE)) {
            avroType.setType(Schema.Type.LONG);
        } else if ((type == Float.class) || (type == Float.TYPE)) {
            avroType.setType(Schema.Type.FLOAT);
        } else if ((type == Double.class) || (type == Double.TYPE)) {
            avroType.setType(Schema.Type.DOUBLE);
        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {
            avroType.setType(Schema.Type.BOOLEAN);
        } else if ((type == Void.class) || (type == Void.TYPE)) {
            avroType.setType(Schema.Type.NULL);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) (Type) type;
            Class raw = (Class) ptype.getRawType();
            java.lang.reflect.Type[] params = ptype.getActualTypeArguments();
            if (Collection.class.isAssignableFrom(raw)) {
                // array
                if (params.length != 1) {
                    throw new AvroTypeException("No array type specified.");
                }
                avroType.setType(Schema.Type.ARRAY);
                AvroType itemType = new AvroType(params[0].getClass());
                parseType(params[0], itemType);
                avroType.setItemType(itemType);
            } else if (Map.class.isAssignableFrom(raw)) {
                // map
                java.lang.reflect.Type key = params[0];
                java.lang.reflect.Type value = params[1];
                if (!(key instanceof Class
                        && CharSequence.class.isAssignableFrom((Class) key))) {
                    throw new AvroTypeException("Map key class not CharSequence: " + key);
                }
                avroType.setType(Schema.Type.MAP);
                AvroType valueType = new AvroType(params[1].getClass());
                parseType(params[1], valueType);
                avroType.setValueType(valueType);
            } else {
                avroType.setType(Schema.Type.RECORD);
            }
        } else if (type instanceof Class) {
            Class clazz = (Class) type;
            if (clazz.isEnum()) {
                //enum
                avroType.setType(Schema.Type.ENUM);
                avroType.setSymbols(Sets.newHashSet(Arrays.stream(clazz.getDeclaredFields()).map(new Function<Field, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable Field input) {
                        return input.getName();
                    }
                }).collect(Collectors.toList())));
            } else {
                // class
                avroType.setType(Schema.Type.RECORD);
            }
        }
    }

    /**
     * Parse logical types from Java type
     * and add to schemas set
     *
     * @param type
     * @param avroType
     * @see <a href="https://avro.apache.org/docs/1.8.1/spec.html#Logical+Types">Logical Types</a>
     */
    protected static void parseLogicalType(Type type, AvroType avroType) {
        if (!(type instanceof Class)) {
            return;
        }
        Class cls = (Class) type;
        Schema schema = null;
        if (BigDecimal.class == cls) {
            Field field = avroType.getField();
            /**
             * precision/scale initial value follows {@link org.hibernate.mapping.Column}
             */
            int precision = 19;
            int scale = 2;
            if (field != null) {
                Column annotation = field.getAnnotation(Column.class);
                if (annotation != null) {
                    precision = annotation.precision() == 0 ? precision : annotation.precision();
                    scale = annotation.scale() == 0 ? scale : annotation.scale();
                }
            }
            LogicalType decimal = LogicalTypes.decimal(precision, scale);
            //add to avroType
            avroType.setLogicalType(decimal);
            //add to schemas
            schema = decimal.addToSchema(Schema.create(Schema.Type.BYTES));
        } else if (UUID.class == cls) {
            LogicalType uuid = LogicalTypes.uuid();
            avroType.setLogicalType(uuid);
            schema = uuid.addToSchema(Schema.create(Schema.Type.STRING));
        } else if (Date.class == cls) {
            LogicalType date = LogicalTypes.date();
            avroType.setLogicalType(date);
            schema = date.addToSchema(Schema.create(Schema.Type.INT));
        } else if (Timestamp.class == cls) {
            LogicalType timestamp = LogicalTypes.timestampMillis();
            avroType.setLogicalType(timestamp);
            schema = timestamp.addToSchema(Schema.create(Schema.Type.LONG));
        }
        if (schema != null) {
            schemas.put(cls.getName(), schema);
        }
    }
}
