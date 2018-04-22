package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.within.ComplexEnum;
import org.apache.avro.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderComplexEnumTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() throws ClassNotFoundException {
        avroSchemaLoader.load(ComplexEnum.class);
        assertEquals("{\"type\":\"enum\",\"name\":\"ComplexEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.cases.within\",\"symbols\":[\"C1\",\"C2\",\"C3\"]}",
                avroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.within.ComplexEnum").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = avroSchemaLoader.parseClass(ComplexEnum.class);
        assertEquals(ComplexEnum.class, record.getClazz());
        assertEquals("ComplexEnum", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.within.ComplexEnum", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases.within", record.getNamespace());
        assertEquals(3, record.getFields().size());
        for (AvroField field : record.getFields()) {
            checkField(field);
        }
    }

    private void checkField(AvroField field) {
        String fieldName = field.getName();
        switch (fieldName) {
            case "C1":
            case "C2":
            case "C3": {
                assertEquals("com.nobodyhub.datalayer.core.cases.within.ComplexEnum", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(ComplexEnum.class, field.getAvroType().getType());
                assertEquals(Schema.Type.ENUM, field.getAvroType().getSchemaType());
                assertEquals(null, field.getAvroType().getLogicalType());
                assertEquals(null, field.getAvroType().getItemType());
                assertEquals(null, field.getAvroType().getValueType());
                break;
            }
            default: {
                assertTrue("Should not come here!", false);
            }

        }
    }
}