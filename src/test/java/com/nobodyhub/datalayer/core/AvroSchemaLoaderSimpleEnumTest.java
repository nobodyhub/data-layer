package com.nobodyhub.datalayer.core;

import com.nobodyhub.datalayer.core.cases.SimpleEnum;
import org.apache.avro.Schema;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ryan
 */
public class AvroSchemaLoaderSimpleEnumTest extends AvroSchemaLoaderTestBase {
    @Test
    public void testLoad() throws ClassNotFoundException {
        AvroSchemaLoader.load(SimpleEnum.class);
        assertEquals("{\"type\":\"enum\",\"name\":\"SimpleEnum\",\"namespace\":\"com.nobodyhub.datalayer.core.cases\",\"symbols\":[\"S1\",\"S2\",\"S3\"]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.cases.SimpleEnum").toString());
    }

    @Test
    public void testParseClass() {
        AvroRecord record = AvroSchemaLoader.parseClass(SimpleEnum.class);
        assertEquals(SimpleEnum.class, record.getClazz());
        assertEquals("SimpleEnum", record.getSimpleName());
        assertEquals("com.nobodyhub.datalayer.core.cases.SimpleEnum", record.getQualifiedName());
        assertEquals("com.nobodyhub.datalayer.core.cases", record.getNamespace());
        assertEquals(3, record.getFields().size());
        for (AvroField field : record.getFields()) {
            checkField(field);
        }
    }

    private void checkField(AvroField field) {
        String fieldName = field.getName();
        switch (fieldName) {
            case "S1":
            case "S2":
            case "S3": {
                assertEquals("com.nobodyhub.datalayer.core.cases.SimpleEnum", field.getQualifiedName());
                assertEquals(false, field.isNullable());
                assertEquals(SimpleEnum.class, field.getAvroType().getType());
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