package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.exception.AvroCoreException;
import lombok.Data;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

import javax.persistence.Column;
import java.lang.reflect.Field;

/**
 * @author Ryan
 */
@Data
public class AvroField {
    /**
     * the {@link Field} of outter class
     */
    private final Field field;
    /**
     * variable name
     */
    private final String name;
    /**
     * whether could be null
     */
    private final boolean nullable;
    /**
     * the Avro type
     */
    private final AvroType avroType;

    public String getQualifiedName() {
        return field.getType().getName();
    }

    public AvroField(Field field) {
        this.field = field;
        this.name = field.getName();
        this.avroType = new AvroType(field);
        this.nullable = field.getAnnotation(Column.class).nullable();
    }

    public SchemaBuilder.FieldAssembler<Schema> assemble(SchemaBuilder.FieldAssembler<Schema> assembler) {
        SchemaBuilder.FieldBuilder<Schema> fieldBuilder = assembler.name(name);

        if (avroType.getLogicalType() == null) {
            SchemaBuilder.BaseFieldTypeBuilder<Schema> typeBuilder = fieldBuilder.type();
            if (nullable) {
                typeBuilder = ((SchemaBuilder.FieldTypeBuilder<Schema>) typeBuilder).nullable();
            }
            switch (avroType.getType()) {
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
                    return fieldBuilder.type(AvroSchemaConverter.getSchema(getQualifiedName())).noDefault();
                }
                case RECORD: {
                    return fieldBuilder.type(AvroSchemaConverter.getSchema(getQualifiedName())).noDefault();
                }
                case MAP: {
                    AvroType valueType = avroType.getValueType();
                    if (valueType.getLogicalType() == null
                            && valueType.getType() != Schema.Type.RECORD
                            && valueType.getType() != Schema.Type.ENUM) {
                        return valueType.assemble(typeBuilder.map().values());
                    } else {
                        return typeBuilder.map()
                                .values(AvroSchemaConverter.getSchema(valueType.getQualifiedName()))
                                .noDefault();
                    }
                }
                case ARRAY: {
                    AvroType itemType = avroType.getItemType();
                    if (itemType.getLogicalType() == null
                            && itemType.getType() != Schema.Type.RECORD
                            && itemType.getType() != Schema.Type.ENUM) {
                        return itemType.assemble(typeBuilder.array().items());
                    } else {
                        return typeBuilder.map()
                                .values(AvroSchemaConverter.getSchema(itemType.getQualifiedName()))
                                .noDefault();
                    }
                }
                default: {
                    //case UNION:
                    //case FIXED:
                    throw new AvroCoreException(String.format("Not support type: '%s'", avroType.getType()));
                }
            }
        } else {
            return fieldBuilder.type(AvroSchemaConverter.getSchema(getQualifiedName())).noDefault();
        }
    }

}
