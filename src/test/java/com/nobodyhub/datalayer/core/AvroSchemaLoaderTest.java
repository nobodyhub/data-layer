package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.annotation.AvroSchemaLoaderConfiguration;
import com.nobodyhub.datalayer.core.cases.PrimitiveClass;
import com.nobodyhub.datalayer.core.cases.SimpleEnum;
import com.nobodyhub.datalayer.core.cases.within.ComplexClassWithoutAnnotation;
import com.nobodyhub.datalayer.core.cases.within.InheritedClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderTest extends AvroSchemaLoaderTestBase {

    /**
     * this configuration will load {@link InheritedClass} and recursively load {@link SimpleEnum} and {@link PrimitiveClass}
     */
    @AvroSchemaLoaderConfiguration(
            basePackage = "com.nobodyhub.datalayer.core.cases.within",
            subTypesOf = com.nobodyhub.datalayer.core.cases.PrimitiveClass.class
    )
    static public class AvroEntityConfigurationSample1 {
    }

    /**
     * this configuration will load all classes within com.nobodyhub.datalayer.core.cases
     * except {@link ComplexClassWithoutAnnotation}.
     * beceause it does not has the {@link javax.persistence.Entity} annotation
     */
    @AvroSchemaLoaderConfiguration(
            basePackage = "com.nobodyhub.datalayer.core.cases.within"
    )
    static public class AvroEntityConfigurationSample2 {
    }

    @Test
    public void testScan() throws ClassNotFoundException {
        avroSchemaLoader.scan();
        // below 3, plus 4 logical types
        assertEquals(12, avroSchemaLoader.schemas.size());
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.within.ComplexEnum"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.within.InheritedClass"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.SimpleEnum"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.ComplexClass"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.PrimitiveClass"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.within.DeepContainer"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.PrimitiveContainerClass"));
        assertEquals(true, avroSchemaLoader.schemas.containsKey("com.nobodyhub.datalayer.core.cases.within.ComplexContainer"));

        assertEquals(8, avroSchemaLoader.records.size());
    }
}