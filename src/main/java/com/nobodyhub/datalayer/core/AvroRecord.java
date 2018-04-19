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
    private final Class<?> clazz;
    private final String name;
    private final String namespace;
    private List<AvroField> fields;

    public AvroRecord(Class<?> clazz) {
        this.clazz = clazz;
        this.namespace = clazz.getPackage().getName();
        this.name = clazz.getSimpleName();
    }

    public Schema toSchema() {
        SchemaBuilder.FieldAssembler<Schema> assembler = SchemaBuilder
                .record(name)
                .namespace(namespace)
                .fields();
        for (AvroField field : fields) {

        }
        return assembler.endRecord();
    }
}
