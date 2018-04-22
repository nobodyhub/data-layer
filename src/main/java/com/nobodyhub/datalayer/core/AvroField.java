package com.nobodyhub.datalayer.core;

import lombok.Data;

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
        return avroType.getQualifiedName();
    }

    public AvroField(Field field) {
        this.field = field;
        this.name = field.getName();
        this.avroType = new AvroType(field);
        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null) {
            this.nullable = annotation.nullable();
        } else {
            //for enum
            this.nullable = false;
        }
    }
}
