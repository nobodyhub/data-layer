package com.nobodyhub.datalayer.core;

import com.google.common.base.Joiner;
import com.nobodyhub.datalayer.core.exception.AvroCoreException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author Ryan
 */
@Data
@RequiredArgsConstructor
public class AvroField {
    /**
     * the {@link Field} of outter class
     */
    private Field field;

    private String name;
    private boolean nullable;

    /**
     * the Java type
     */
    private final Class<?> cls;
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
    private AvroField itemType;
    /**
     * value type when {@link this#type} is {@link Schema.Type#MAP}
     * key type is required to be {@link Schema.Type#STRING}
     */
    private AvroField valueType;
    /**
     * symbols when {@link this#type} is {@link Schema.Type#ENUM}
     */
    private Set<String> symbols;

    @SuppressWarnings("unchecked")
    public SchemaBuilder.FieldAssembler<Schema> assemble(SchemaBuilder.TypeBuilder<?> typeBuilder) {
        if (nullable) {
            typeBuilder = (SchemaBuilder.TypeBuilder<?>) typeBuilder.nullable();
        }
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
                if (valueType.logicalType == null
                        && valueType.type != Schema.Type.RECORD) {
                    return assemble(typeBuilder.map().values());
                } else {
                    return ((SchemaBuilder.MapDefault<Schema>) typeBuilder.map()
                            .values(AvroSchemaConverter.getSchema(valueType.getName())))
                            .noDefault();
                }
            }
            case ARRAY: {
                if (valueType.logicalType == null) {
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

    public SchemaBuilder.FieldAssembler<Schema> assemble(SchemaBuilder.FieldAssembler<Schema> assembler) {
        SchemaBuilder.FieldBuilder<Schema> fieldBuilder = assembler.name(name);

        if (logicalType == null) {
            SchemaBuilder.BaseFieldTypeBuilder<Schema> typeBuilder = fieldBuilder.type();
            if (nullable) {
                typeBuilder = ((SchemaBuilder.FieldTypeBuilder<Schema>) typeBuilder).nullable();
            }
            switch (type) {
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
                case NULL: {
                    return typeBuilder.nullType().noDefault();
                }
                case BYTES: {
                    return typeBuilder.bytesType().noDefault();
                }
                case STRING: {
                    return typeBuilder.stringType().noDefault();
                }
                case ENUM: {
                    return fieldBuilder.type(AvroSchemaConverter.getSchema(name)).noDefault();
                }
                case RECORD: {
                    return fieldBuilder.type(AvroSchemaConverter.getSchema(name)).noDefault();
                }
                case MAP: {
                    if (valueType.logicalType == null
                            && valueType.type != Schema.Type.RECORD
                            && valueType.type != Schema.Type.ENUM) {
                        return valueType.assemble(typeBuilder.map().values());
                    } else {
                        return typeBuilder.map()
                                .values(AvroSchemaConverter.getSchema(valueType.getName()))
                                .noDefault();
                    }
                }
                case ARRAY: {
                    if (itemType.logicalType == null
                            && itemType.type != Schema.Type.RECORD
                            && itemType.type != Schema.Type.ENUM) {
                        return itemType.assemble(typeBuilder.array().items());
                    } else {
                        return typeBuilder.map()
                                .values(AvroSchemaConverter.getSchema(itemType.getName()))
                                .noDefault();
                    }
                }

                default:
                case UNION:
                case FIXED: {
                    throw new AvroCoreException(String.format("Not support type: '%s'", type));
                }
            }
        } else {
            return fieldBuilder.type(AvroSchemaConverter.getSchema(name)).noDefault();
        }
    }
}
