package com.nobodyhub.datalayer.core;

import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectDatumWriter;

/**
 * @author Ryan
 */
public class AvroDatumWriter<T> extends ReflectDatumWriter<T> {
    public AvroDatumWriter() {
        this(AvroData.get());
    }

    public AvroDatumWriter(Class<T> c) {
        this(c, AvroData.get());
    }

    public AvroDatumWriter(Class<T> c, AvroData data) {
        this(data.getSchema(c), data);
    }

    public AvroDatumWriter(Schema root) {
        this(root, AvroData.get());
    }

    public AvroDatumWriter(Schema root, AvroData avroData) {
        super(root, avroData);
    }

    protected AvroDatumWriter(AvroData avroData) {
        super(avroData);
    }
}
