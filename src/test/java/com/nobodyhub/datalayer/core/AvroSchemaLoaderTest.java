package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.annotation.AvroSchemaLoaderConfiguration;
import com.nobodyhub.datalayer.core.cases.ComplexClass;
import com.nobodyhub.datalayer.core.cases.PrimitiveClass;
import com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass;
import com.nobodyhub.datalayer.core.cases.SimpleEnum;
import com.nobodyhub.datalayer.core.cases.within.ComplexEnum;
import com.nobodyhub.datalayer.core.cases.within.DeepContainer;
import com.nobodyhub.datalayer.core.cases.within.InheritedClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderTest extends AvroSchemaLoaderTestBase {

    /**
     * this configuration will load {@link InheritedClass}
     * and recursively load {@link SimpleEnum} and {@link PrimitiveClass}
     */
    @AvroSchemaLoaderConfiguration(
            basePackage = "com.nobodyhub.datalayer.core.cases.within",
            subTypesOf = com.nobodyhub.datalayer.core.cases.PrimitiveClass.class
    )
    static public class AvroEntityConfigurationSample1 {
    }

    /**
     * this configuration will load only {@link DeepContainer}
     * and recursively load {@link SimpleEnum}, {@link PrimitiveClass}, {@link InheritedClass}, {@link ComplexClass}, {@link ComplexEnum} and {@link PrimitiveContainerClass}
     */
    @AvroSchemaLoaderConfiguration(
            basePackage = "com.nobodyhub.datalayer.core.cases.within"
    )
    static public class AvroEntityConfigurationSample2 {
    }

    @Test
    public void testScan() throws ClassNotFoundException {
        avroSchemaLoader.scan();
        assertEquals(11, avroSchemaLoader.schemas.size());
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.within.ComplexEnum"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.within.InheritedClass"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.SimpleEnum"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.ComplexClass"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.PrimitiveClass"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.within.DeepContainer"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass"));

        assertEquals(7, avroSchemaLoader.records.size());
    }
}