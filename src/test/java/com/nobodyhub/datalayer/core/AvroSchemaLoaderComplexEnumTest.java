package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.AvroField;
import com.nobodyhub.datalayer.core.AvroRecord;
import com.nobodyhub.datalayer.core.AvroSchemaLoader;
import com.nobodyhub.datalayer.core.cases.ComplexEnum;
import org.apache.avro.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderComplexEnumTest {
    @Test
    public void testLoad() {
        AvroSchemaLoader.load(ComplexEnum.class);
        assertEquals("{\"type\":\"enum\",\"name\":\"ComplexEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"symbols\":[\"C1\",\"C2\",\"C3\"]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.ComplexEnum").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = AvroSchemaLoader.parseClass(ComplexEnum.class);
        assertEquals(ComplexEnum.class, record.getClazz());
        assertEquals("ComplexEnum", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.ComplexEnum", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases", record.getNamespace());
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
                assertEquals("com.nobodyhub.datalayer.core.cases.ComplexEnum", field.getQualifiedName());
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