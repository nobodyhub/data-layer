package com.nobodyhub.datalayer.core;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nobodyhub.datalayer.core.annotation.AvroSchemaLoaderConfiguration;
import com.nobodyhub.datalayer.core.exception.AvroCoreException;
import org.apache.avro.*;
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
import java.util.logging.Logger;

/**
 * Load {@link javax.persistence.Entity} class and create Avro Schema
 *
 * @author Ryan
 */
public final class AvroSchemaLoader {

    private Logger logger = Logger.getLogger(AvroSchemaLoader.class.getSimpleName());
    protected final Map<String, Schema> schemas = Maps.newHashMap();
    protected final Map<String, AvroRecord> records = Maps.newHashMap();

    /**
     * Scan the classloader to load classes for Avro Schemas
     *
     * @throws ClassNotFoundException
     */
    public void scan() throws ClassNotFoundException {
        logger.info("Start to scan classes for AvroSchema");
        //get annotation settings
        Reflections configurationReflection = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forClassLoader())
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
        List<Class<?>> configurations = Lists.newArrayList(configurationReflection.getTypesAnnotatedWith(AvroSchemaLoaderConfiguration.class));
        if (configurations.isEmpty()) {
            throw new RuntimeException(String.format("Can not find configuration %s within classloaders %s!",
                    AvroSchemaLoaderConfiguration.class.getName(),
                    Joiner.on(", ").join(ClasspathHelper.classLoaders())));
        }
        for (Class<?> configCls : configurations) {
            logger.info(AvroSchemaLoaderConfiguration.class.getSimpleName() + " found on Class: " + configCls.getSimpleName());
            AvroSchemaLoaderConfiguration configuration = configCls.getAnnotation(AvroSchemaLoaderConfiguration.class);
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
    }

    public void clear() {
        this.schemas.clear();
        this.records.clear();
    }

    protected Schema getSchema(String qualifiedName) throws ClassNotFoundException {
        Schema schema = schemas.get(qualifiedName);
        if (schema == null) {
            AvroRecord record = records.get(qualifiedName);
            if (record == null) {
                load(Class.forName(qualifiedName));
                record = records.get(qualifiedName);
            }
            schema = record.toSchema(this);
        }
        return schema;
    }

    protected void load(Class<?>... classes) throws ClassNotFoundException {
        List<AvroRecord> newRecords = Lists.newArrayList();
        for (Class<?> clazz : classes) {
            newRecords.add(parseClass(clazz));
        }
        for (AvroRecord record : newRecords) {
            schemas.put(record.getQualifiedName(), record.toSchema(this));
        }
    }

    protected AvroRecord parseClass(Class<?> clazz) {
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
    protected void fillFieldInfo(AvroRecord record) {
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

    protected void parseType(Type type, AvroType avroType) {
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
    protected void parseBasicType(Type type, AvroType avroType) {
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
    protected void parseLogicalType(Type type, AvroType avroType) {
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

    protected <R extends SchemaBuilder.FieldDefault> SchemaBuilder.FieldAssembler assemble(SchemaBuilder.TypeBuilder<R> typeBuilder, AvroType avroType) throws ClassNotFoundException {
        switch (avroType.getSchemaType()) {
            case INT: {
                return typeBuilder.intType().noDefault();
            }
            case LONG: {
                return typeBuilder.longType().noDefault();
            }
            case FLOAT: {
                return typeBuilder.floatType().noDefault();
            }
            case DOUBLE: {
                return typeBuilder.doubleType().noDefault();
            }
            case BOOLEAN: {
                return typeBuilder.booleanType().noDefault();
            }
            case BYTES: {
                return typeBuilder.bytesType().noDefault();
            }
            case STRING: {
                return typeBuilder.stringType().noDefault();
            }
            case MAP: {
                if (avroType.getValueType().getLogicalType() == null
                        && avroType.getValueType().getSchemaType() != Schema.Type.RECORD
                        && avroType.getValueType().getSchemaType() != Schema.Type.ENUM) {
                    return assemble(typeBuilder.map().values(), avroType.getValueType());
                } else {
                    return typeBuilder.map()
                            .values(getSchema(avroType.getValueType().getQualifiedName()))
                            .noDefault();
                }
            }
            case ARRAY: {
                if (avroType.getItemType().getLogicalType() == null
                        && avroType.getItemType().getSchemaType() != Schema.Type.RECORD
                        && avroType.getItemType().getSchemaType() != Schema.Type.ENUM) {
                    return assemble(typeBuilder.array().items(), avroType.getItemType());
                } else {
                    return typeBuilder.map()
                            .values(getSchema(avroType.getItemType().getQualifiedName()))
                            .noDefault();
                }
            }
            default: {
                //case UNION: can not handle
                //case FIXED: can not handle
                //case NULL: can not handle
                //case RECORD: handled by AvroField#assemble
                //case ENUM: handled by AvroField#assemble
                throw new AvroCoreException(String.format("Not support type: '%s'", avroType.getSchemaType()));
            }
        }
    }

    protected SchemaBuilder.FieldAssembler<Schema> assemble(SchemaBuilder.FieldAssembler<Schema> assembler, AvroField field) throws ClassNotFoundException {
        SchemaBuilder.FieldBuilder<Schema> fieldBuilder = assembler.name(field.getName());

        if (field.getAvroType().getLogicalType() == null) {
            SchemaBuilder.BaseFieldTypeBuilder<Schema> typeBuilder = fieldBuilder.type();
            if (field.isNullable()) {
                typeBuilder = ((SchemaBuilder.FieldTypeBuilder<Schema>) typeBuilder).nullable();
            }
            switch (field.getAvroType().getSchemaType()) {
                case INT: {
                    return typeBuilder.intType().noDefault();
                }
                case LONG: {
                    return typeBuilder.longType().noDefault();
                }
                case FLOAT: {
                    return typeBuilder.floatType().noDefault();
                }
                case DOUBLE: {
                    return typeBuilder.doubleType().noDefault();
                }
                case BOOLEAN: {
                    return typeBuilder.booleanType().noDefault();
                }
                case BYTES: {
                    return typeBuilder.bytesType().noDefault();
                }
                case STRING: {
                    return typeBuilder.stringType().noDefault();
                }
                case ENUM: {
                    return fieldBuilder.type(getSchema(field.getQualifiedName())).noDefault();
                }
                case RECORD: {
                    return fieldBuilder.type(getSchema(field.getQualifiedName())).noDefault();
                }
                case MAP: {
                    AvroType valueType = field.getAvroType().getValueType();
                    if (valueType.getLogicalType() == null
                            && valueType.getSchemaType() != Schema.Type.RECORD
                            && valueType.getSchemaType() != Schema.Type.ENUM) {
                        return assemble(typeBuilder.map().values(), valueType);
                    } else {
                        return typeBuilder.map()
                                .values(getSchema(valueType.getQualifiedName()))
                                .noDefault();
                    }
                }
                case ARRAY: {
                    AvroType itemType = field.getAvroType().getItemType();
                    if (itemType.getLogicalType() == null
                            && itemType.getSchemaType() != Schema.Type.RECORD
                            && itemType.getSchemaType() != Schema.Type.ENUM) {
                        return assemble(typeBuilder.array().items(), itemType);
                    } else {
                        return typeBuilder.array()
                                .items(getSchema(itemType.getQualifiedName()))
                                .noDefault();
                    }
                }
                default: {
                    //case UNION:
                    //case FIXED:
                    //case NULL;
                    throw new AvroCoreException(String.format("Not support type: '%s'", field.getAvroType().getSchemaType()));
                }
            }
        } else {
            return fieldBuilder.type(getSchema(field.getQualifiedName())).noDefault();
        }
    }
}
