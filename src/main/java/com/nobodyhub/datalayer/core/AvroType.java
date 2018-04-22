package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.exception.AvroCoreException;
import lombok.Data;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author Ryan
 */
@Data
public class AvroType {
    /**
     * the Java class
     */
    private final Type type;
    /**
     * the {@link Field} of outter class
     */
    private Field field;
    /**
     * full qualified class qualifiedName
     */
    private final String qualifiedName;
    /**
     * the string representing the {@link Schema.Type} or {@link LogicalTypes}
     */
    private Schema.Type schemaType;
    /**
     * the logical type
     *
     * @see {@link LogicalType}
     */
    private LogicalType logicalType;
    /**
     * element type when {@link this#schemaType} is {@link Schema.Type#ARRAY}
     */
    private AvroType itemType;
    /**
     * value type when {@link this#schemaType} is {@link Schema.Type#MAP}
     * key type is required to be {@link Schema.Type#STRING}
     */
    private AvroType valueType;

    public AvroType(Type type) {
        this.type = type;
        this.qualifiedName = type.getTypeName();
    }

    public AvroType(Field field) {
        this.field = field;
        this.type = field.getType();
        this.qualifiedName = type.getTypeName();
    }

    public <R extends SchemaBuilder.FieldDefault> SchemaBuilder.FieldAssembler assemble(SchemaBuilder.TypeBuilder<R> typeBuilder) throws ClassNotFoundException {
        switch (schemaType) {
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
                if (valueType.getLogicalType() == null
                        && valueType.getSchemaType() != Schema.Type.RECORD
                        && valueType.getSchemaType() != Schema.Type.ENUM) {
                    return valueType.assemble(typeBuilder.map().values());
                } else {
                    return typeBuilder.map()
                            .values(AvroSchemaLoader.getSchema(valueType.getQualifiedName()))
                            .noDefault();
                }
            }
            case ARRAY: {
                if (itemType.getLogicalType() == null
                        && itemType.getSchemaType() != Schema.Type.RECORD
                        && itemType.getSchemaType() != Schema.Type.ENUM) {
                    return itemType.assemble(typeBuilder.array().items());
                } else {
                    return typeBuilder.map()
                            .values(AvroSchemaLoader.getSchema(itemType.getQualifiedName()))
                            .noDefault();
                }
            }
            default: {
                //case UNION: can not handle
                //case FIXED: can not handle
                //case NULL: can not handle
                //case RECORD: handled by AvroField#assemble
                //case ENUM: handled by AvroField#assemble
                throw new AvroCoreException(String.format("Not support type: '%s'", schemaType));
            }
        }
    }
}
