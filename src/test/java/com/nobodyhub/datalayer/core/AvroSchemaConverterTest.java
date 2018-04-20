package com.nobodyhub.datalayer.core;

import org.junit.Test;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan
 */
public class AvroSchemaConverterTest {
    @Test
    public void testParseClass() {
        AvroRecord primitiveClass = AvroSchemaConverter.parseClass(PrimitiveClass.class);
        assertEquals(PrimitiveClass.class, primitiveClass.getClazz());
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"aString\",\"type\":\"string\"},{\"name\":\"aByte\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"aFloat\",\"type\":\"float\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"stringSet\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"integerList\",\"type\":{\"type\":\"array\",\"items\":\"int\"}},{\"name\":\"stringMap\",\"type\":{\"type\":\"map\",\"values\":\"double\"}},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":5}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimestamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}}]}",
                primitiveClass.toSchema().toString(false));

        AvroRecord country = AvroSchemaConverter.parseClass(Country.class);
        assertEquals(Country.class, country.getClazz());
        assertEquals("{\"type\":\"enum\",\"name\":\"Country\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"symbols\":[\"CN\",\"US\",\"JP\"]}",
                country.toSchema().toString(false));

        AvroRecord combinedPrimitiveClass = AvroSchemaConverter.parseClass(CombinedPrimitiveClass.class);
        assertEquals(CombinedPrimitiveClass.class, combinedPrimitiveClass.getClazz());
        assertEquals("{\"type\":\"record\",\"name\":\"CombinedPrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"recordSet\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":\"int\"}}},{\"name\":\"listOfList\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"string\"}}},{\"name\":\"map2List\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"array\",\"items\":\"string\"}},\"null\"]}]}",
                combinedPrimitiveClass.toSchema().toString(false));

    }

    @Test
    public void testLoad() {
        AvroSchemaConverter.load(ComplexClass.class, TypedClass.class, PrimitiveClass.class, Country.class);
        assertEquals(4, AvroSchemaConverter.records.size());
        assertEquals(8, AvroSchemaConverter.schemas.size());
        assertEquals("{\"type\":\"record\",\"name\":\"TypedClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"field\",\"type\":\"string\"}]}",
                AvroSchemaConverter.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaConverterTest$TypedClass").toString());
        assertEquals("",
                AvroSchemaConverter.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaConverterTest$ComplexClass").toString());
        assertEquals("",
                AvroSchemaConverter.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaConverterTest$PrimitiveClass").toString());
        assertEquals("",
                AvroSchemaConverter.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaConverterTest$Country").toString());

    }

    private static class ComplexClass extends PrimitiveClass {
        @Column(nullable = false)
        private PrimitiveClass aRecord;
        @Column
        private TypedClass<String> aTypedRecord;
        @Column(nullable = false)
        private Country countryEnum;
    }

    private static class TypedClass<T> {
        @Column(nullable = false)
        private String field;
    }

    private static class PrimitiveClass {
        @Column(nullable = false)
        private String aString;
        @Column
        private ByteBuffer aByte;
        @Column(nullable = false)
        private Integer aInt;
        @Column
        private Long aLong;
        @Column(nullable = false)
        private Float aFloat;
        @Column
        private Double aDouble;
        @Column
        private Boolean aBoolean;
        @Column
        private Set<String> stringSet;
        @Column(nullable = false)
        private List<Integer> integerList;
        @Column(nullable = false)
        private Map<String, Double> stringMap;
        @Column(precision = 10, scale = 5)
        private BigDecimal aBigDecimal;
        @Column(nullable = false)
        private UUID aUuid;
        @Column
        private Date aDate;
        @Column
        private Timestamp aTimestamp;
    }

    private static class CombinedPrimitiveClass {
        @Column(nullable = false)
        Set<Map<String, Integer>> recordSet;
        @Column(nullable = false)
        List<List<String>> listOfList;
        @Column
        Map<String, List<String>> map2List;
    }

    private enum Country {
        CN,
        US,
        JP
    }


}