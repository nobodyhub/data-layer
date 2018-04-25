package com.nobodyhub.datalayer.core.service.common;

import lombok.EqualsAndHashCode;
import org.apache.avro.reflect.AvroIgnore;

/**
 * returned as an empty result
 *
 * @author Ryan
 */
@EqualsAndHashCode
public class AvroVoidEntity implements AvroEntity {

    @AvroIgnore
    public static final AvroVoidEntity INSTANCE = new AvroVoidEntity();

    private AvroVoidEntity() {
    }

    public static AvroVoidEntity get() {
        return INSTANCE;
    }
}