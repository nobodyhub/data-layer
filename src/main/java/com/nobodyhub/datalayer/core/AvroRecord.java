package com.nobodyhub.datalayer.core;

import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Avro Schema
 *
 * @author Ryan
 */
@Data
@RequiredArgsConstructor
public class AvroRecord {
    private final Class<?> clazz;
    private String type;
    private String name;
    private String namespace;
    private List<AvroField> fields;
}
