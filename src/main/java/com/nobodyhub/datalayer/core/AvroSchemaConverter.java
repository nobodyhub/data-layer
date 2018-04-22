package com.nobodyhub.datalayer.core;

import lombok.RequiredArgsConstructor;
import org.apache.avro.generic.GenericRecord;

/**
 * @author Ryan
 */
@RequiredArgsConstructor
public class AvroSchemaConverter {
    private final AvroSchemaLoader loader;

    public GenericRecord from(AvroEntity avroEntity) {
        return null;

    }

    public AvroEntity to(GenericRecord record) {
        return null;
    }
}
