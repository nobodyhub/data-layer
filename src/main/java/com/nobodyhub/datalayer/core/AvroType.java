package com.nobodyhub.datalayer.core;

import lombok.Data;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

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
}
