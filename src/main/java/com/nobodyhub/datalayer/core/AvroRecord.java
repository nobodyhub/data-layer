package com.nobodyhub.datalayer.core;

import com.google.common.collect.Lists;
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
        this.fields = Lists.newArrayList();
    }

    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    public String getQualifiedName() {
        return clazz.getName();
    }

    public void addField(AvroField field) {
        this.fields.add(field);
    }

    public Schema toSchema() {
        SchemaBuilder.FieldAssembler<Schema> assembler = SchemaBuilder
                .record(getSimpleName())
                .namespace(namespace)
                .fields();
        for (AvroField field : fields) {
            assembler = field.assemble(assembler);
        }
        return assembler.endRecord();
    }

}
