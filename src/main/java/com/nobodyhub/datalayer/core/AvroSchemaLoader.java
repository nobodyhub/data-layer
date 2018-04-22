package com.nobodyhub.datalayer.core;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nobodyhub.datalayer.core.annotation.AvroSchemaLoaderConfiguration;
import org.apache.avro.AvroTypeException;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.specific.SpecificData;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public final class AvroSchemaLoader {
    private static Logger logger = Logger.getLogger(AvroSchemaLoader.class.getSimpleName());
    public static final Map<String, Schema> schemas = Maps.newHashMap();
    public static final Map<String, AvroRecord> records = Maps.newHashMap();
    public static AvroSchemaLoaderConfiguration configuration;

    private AvroSchemaLoader() {
    }

    protected static void scan() throws ClassNotFoundException {
        //get annotation settings
        Reflections configurationReflection = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forClassLoader())
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
        List<Class<?>> configurations = Lists.newArrayList(configurationReflection.getTypesAnnotatedWith(AvroSchemaLoaderConfiguration.class));
        if (configurations.isEmpty()) {
            logger.log(Level.SEVERE,
                    String.format("Can not find configuration %s within classloader %s!", AvroSchemaLoaderConfiguration.class.getName(), Joiner.on(", ").join(ClasspathHelper.classLoaders())));
        } else if (configurations.size() > 1) {
            logger.log(Level.WARNING,
                    String.format("More than 1 configurations is found, only %s will be used!", configurations.get(0)));
        }
        configuration = configurations.get(0).getAnnotation(AvroSchemaLoaderConfiguration.class);
        //get the target classes in base package
        Reflections targetClassReflection = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(configuration.basePackage()))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
        //filter by annotation
        Set<Class<?>> targetCls = targetClassReflection.getTypesAnnotatedWith(configuration.annotatedWith());
        //filter by subType
        targetCls.retainAll(targetClassReflection.getSubTypesOf(configuration.subTypesOf()));
        //load target as schema
        load(targetCls.toArray(new Class<?>[0]));
    }

    protected static Schema getSchema(String qualifiedName) throws ClassNotFoundException {
        Schema schema = schemas.get(qualifiedName);
        if (schema == null) {
            AvroRecord record = records.get(qualifiedName);
            if (record == null) {
                load(Class.forName(qualifiedName));
                record = records.get(qualifiedName);
            }
            schema = record.toSchema();
        }
        return schema;
    }

    protected static void load(Class<?>... classes) throws ClassNotFoundException {
        List<AvroRecord> newRecords = Lists.newArrayList();
        for (Class<?> clazz : classes) {
            newRecords.add(parseClass(clazz));
        }
        for (AvroRecord record : newRecords) {
            schemas.put(record.getQualifiedName(), record.toSchema());
        }
    }

    protected static AvroRecord parseClass(Class<?> clazz) {
        AvroRecord record = new AvroRecord(clazz);
        fillFieldInfo(record);
        records.put(record.getQualifiedName(), record);
        return record;
    }

    /**
     * The target field should be either:
     * 1. Has {@link Column} annotation
     * 2. fields of a enum
     *
     * @param record
     */
    protected static void fillFieldInfo(AvroRecord record) {
        Class<?> clz = record.getClazz();
        while (clz != null) {
            Field[] fields = null;
            if (clz.isEnum()) {
                /**
                 * the accessible public fields
                 * which excludes {@link Enum$VALUES}
                 */
                fields = clz.getFields();
            } else {
                /**
                 * includes public, protected, default
                 * (package) access, and private fields, but excludes inherited fields.
                 */
                fields = clz.getDeclaredFields();
            }
            for (Field field : fields) {
                Column annotation = field.getAnnotation(Column.class);
                if (annotation != null || clz.isEnum()) {
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
            avroType.setSchemaType(Schema.Type.STRING);
        } else if (type == ByteBuffer.class) {
            avroType.setSchemaType(Schema.Type.BYTES);
        } else if ((type == Integer.class) || (type == Integer.TYPE)) {
            avroType.setSchemaType(Schema.Type.INT);
        } else if ((type == Long.class) || (type == Long.TYPE)) {
            avroType.setSchemaType(Schema.Type.LONG);
        } else if ((type == Float.class) || (type == Float.TYPE)) {
            avroType.setSchemaType(Schema.Type.FLOAT);
        } else if ((type == Double.class) || (type == Double.TYPE)) {
            avroType.setSchemaType(Schema.Type.DOUBLE);
        } else if ((type == Boolean.class) || (type == Boolean.TYPE)) {
            avroType.setSchemaType(Schema.Type.BOOLEAN);
        } else if ((type == Void.class) || (type == Void.TYPE)) {
            avroType.setSchemaType(Schema.Type.NULL);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) (Type) type;
            Class raw = (Class) ptype.getRawType();
            java.lang.reflect.Type[] params = ptype.getActualTypeArguments();
            if (Collection.class.isAssignableFrom(raw)) {
                // array
                if (params.length != 1) {
                    throw new AvroTypeException("No array type specified.");
                }
                avroType.setSchemaType(Schema.Type.ARRAY);
                AvroType itemType = new AvroType(params[0]);
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
                avroType.setSchemaType(Schema.Type.MAP);
                AvroType valueType = new AvroType(params[1]);
                parseType(params[1], valueType);
                avroType.setValueType(valueType);
            } else {
                avroType.setSchemaType(Schema.Type.RECORD);
            }
        } else if (type instanceof Class) {
            Class clazz = (Class) type;
            if (clazz.isEnum()) {
                avroType.setSchemaType(Schema.Type.ENUM);
                //enum
//                if (avroType.getField() != null
//                        && avroType.getField().getAnnotation(Column.class) != null) {
//                    // as class member
//                    avroType.setType(Schema.Type.ENUM);
//                } else {
//                    // as enum member
//                    avroType.setType(Schema.Type.STRING);
//                }
            } else {
                // class
                avroType.setSchemaType(Schema.Type.RECORD);
            }
        } else {
            avroType.setSchemaType(Schema.Type.RECORD);
        }
    }

    /**
     * Parse logical types from Java type and add to schemas set
     * Supported Logical Types are:
     * - {@link LogicalTypes#DECIMAL} - {@link BigDecimal}
     * - {@link LogicalTypes#UUID} - {@link UUID}
     * - {@link LogicalTypes#DATE} - {@link Date}
     * - {@link LogicalTypes#TIME_MILLIS} - {@link Timestamp}
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
        // add to schema pool
        if (schema != null) {
            schemas.put(cls.getName(), schema);
        }
    }
}
