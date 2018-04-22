package com.nobodyhub.datalayer.core;

import org.junit.After;

/**
 * @author Ryan
 */
public abstract class AvroSchemaLoaderTestBase {
    protected final AvroSchemaLoader avroSchemaLoader = new AvroSchemaLoader();

    @After
    public void setup() {
        avroSchemaLoader.clear();
    }
}