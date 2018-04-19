package com.nobodyhub.datalayer.core;

import lombok.Data;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

import java.util.List;

/**
 * Avro Schema
 *
 * @author Ryan
 */
@Data
public class AvroRecord {
    /**
     * Java class
     */
    private final Class<?> clazz;
    /**
     * Java package
     */
    private final String namespace;
    /**
     * Fields contained by {@link this#clazz}
     */
    private List<AvroField> fields;

    public AvroRecord(Class<?> clazz) {
        this.clazz = clazz;
        this.namespace = clazz.getPackage().getName();
    }

    public Schema toSchema() {
        SchemaBuilder.FieldAssembler<Schema> assembler = SchemaBuilder
                .record(getSimpleName())
                .namespace(namespace)
                .fields();
        for (AvroField field : fields) {

        }
        return assembler.endRecord();
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public String getQualifiedName() {
        return clazz.getName();
    }
}
