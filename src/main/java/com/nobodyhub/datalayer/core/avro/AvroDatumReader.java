package com.nobodyhub.datalayer.core.avro;

import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectDatumReader;

/**
 * @author Ryan
 */
public class AvroDatumReader<T> extends ReflectDatumReader<T> {
    public AvroDatumReader() {
        this(null, null, AvroData.get());
    }

    /**
     * Construct for reading instances of a class.
     */
    public AvroDatumReader(Class<T> c) {
        this(new AvroData(c.getClassLoader()));
        setSchema(getSpecificData().getSchema(c));
    }

    /**
     * Construct where the writer's and reader's schemas are the same.
     */
    public AvroDatumReader(Schema root) {
        this(root, root, AvroData.get());
    }

    /**
     * Construct given writer's and reader's schema.
     */
    public AvroDatumReader(Schema writer, Schema reader) {
        this(writer, reader, AvroData.get());
    }

    /**
     * Construct given writer's and reader's schema and the data model.
     */
    public AvroDatumReader(Schema writer, Schema reader, AvroData data) {
        super(writer, reader, data);
    }

    /**
     * Construct given a {@link AvroData}.
     */
    public AvroDatumReader(AvroData data) {
        super(data);
    }
}
