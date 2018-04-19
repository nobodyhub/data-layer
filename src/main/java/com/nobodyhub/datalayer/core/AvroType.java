package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.exception.AvroCoreException;
import lombok.Data;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author Ryan
 */
@Data
public class AvroType {
    /**
     * the Java class
     */
    private final Class<?> cls;
    /**
     * the {@link Field} of outter class
     */
    private Field field;
    /**
     * full qualified class name
     */
    private final String name;
    /**
     * the string representing the {@link Schema.Type} or {@link LogicalTypes}
     */
    private Schema.Type type;
    /**
     * the logical type
     *
     * @see {@link LogicalType}
     */
    private LogicalType logicalType;
    /**
     * element type when {@link this#type} is {@link Schema.Type#ARRAY}
     */
    private AvroType itemType;
    /**
     * value type when {@link this#type} is {@link Schema.Type#MAP}
     * key type is required to be {@link Schema.Type#STRING}
     */
    private AvroType valueType;
    /**
     * symbols when {@link this#type} is {@link Schema.Type#ENUM}
     */
    private Set<String> symbols;
    /**
     * precision when {@link this#logicalType} is {@link LogicalTypes#DECIMAL}
     */
    private int precision;
    /**
     * scale when {@link this#logicalType} is {@link LogicalTypes#DECIMAL}
     */
    private int scale;

    public AvroType(Class<?> cls) {
        this.cls = cls;
        this.name = cls.getName();
    }

    public AvroType(Field field) {
        this.field = field;
        this.cls = field.getType();
        this.name = cls.getName();
    }

    @SuppressWarnings("unchecked")
    public SchemaBuilder.FieldAssembler<Schema> assemble(SchemaBuilder.TypeBuilder<?> typeBuilder) {
        switch (type) {
            case INT: {
                return ((SchemaBuilder.IntDefault<Schema>) typeBuilder.intType()).noDefault();
            }
            case LONG: {
                return ((SchemaBuilder.LongDefault<Schema>) typeBuilder.longType()).noDefault();
            }
            case FLOAT: {
                return ((SchemaBuilder.FloatDefault<Schema>) typeBuilder.floatType()).noDefault();
            }
            case DOUBLE: {
                return ((SchemaBuilder.DoubleDefault<Schema>) typeBuilder.doubleType()).noDefault();
            }
            case BOOLEAN: {
                return ((SchemaBuilder.BooleanDefault<Schema>) typeBuilder.booleanType()).noDefault();
            }
            case NULL: {
                return ((SchemaBuilder.NullDefault<Schema>) typeBuilder.nullType()).noDefault();
            }
            case BYTES: {
                return ((SchemaBuilder.BytesDefault<Schema>) typeBuilder.bytesType()).noDefault();
            }
            case STRING: {
                return ((SchemaBuilder.StringDefault<Schema>) typeBuilder.stringType()).noDefault();
            }
            case MAP: {
                if (valueType.getLogicalType() == null
                        && valueType.getType() != Schema.Type.RECORD
                        && valueType.getType() != Schema.Type.ENUM) {
                    return assemble(typeBuilder.map().values());
                } else {
                    return ((SchemaBuilder.MapDefault<Schema>) typeBuilder.map()
                            .values(AvroSchemaConverter.getSchema(valueType.getName())))
                            .noDefault();
                }
            }
            case ARRAY: {
                if (itemType.getLogicalType() == null
                        && itemType.getType() != Schema.Type.RECORD
                        && itemType.getType() != Schema.Type.ENUM) {
                    return assemble(typeBuilder.array().items());
                } else {
                    return ((SchemaBuilder.ArrayDefault<Schema>) typeBuilder.map()
                            .values(AvroSchemaConverter.getSchema(itemType.getName())))
                            .noDefault();
                }
            }
            default:
            case UNION:
            case FIXED: {
                throw new AvroCoreException(String.format("Not support type: '%s'", type));
            }
        }
    }
}
