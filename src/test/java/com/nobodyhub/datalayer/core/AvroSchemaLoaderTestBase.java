package com.nobodyhub.datalayer.core;

import org.junit.After;

/**
 * @author Ryan
 */
public abstract class AvroSchemaLoaderTestBase {
    @After
    public void setup() {
        AvroSchemaLoader.schemas.clear();
        AvroSchemaLoader.records.clear();
    }
}