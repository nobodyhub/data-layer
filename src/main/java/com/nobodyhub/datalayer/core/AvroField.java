package com.nobodyhub.datalayer.core;

import java.util.List;

/**
 * Avro field
 *
 * @author Ryan
 */
public class AvroField {
    private String type;
    private String name;
    private boolean nullable;
    private Object dftVal;
    List<AvroField> subFields;

}
