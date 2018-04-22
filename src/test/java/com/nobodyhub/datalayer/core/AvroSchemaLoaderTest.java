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
public class AvroSchemaLoaderTest {
    @Test
    public void testScan() throws ClassNotFoundException {
        AvroSchemaLoader.scan();
    }

    @Test
    public void testParseClass() throws ClassNotFoundException {
        AvroRecord primitiveClass = AvroSchemaLoader.parseClass(PrimitiveClass.class);
        assertEquals(PrimitiveClass.class, primitiveClass.getClazz());
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"aString\",\"type\":\"string\"},{\"name\":\"aByte\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"aFloat\",\"type\":\"float\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"stringSet\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"integerList\",\"type\":{\"type\":\"array\",\"items\":\"int\"}},{\"name\":\"stringMap\",\"type\":{\"type\":\"map\",\"values\":\"double\"}},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":5}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimestamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}}]}",
                primitiveClass.toSchema().toString(false));

        AvroRecord country = AvroSchemaLoader.parseClass(Country.class);
        assertEquals(Country.class, country.getClazz());
        assertEquals("{\"type\":\"enum\",\"name\":\"Country\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"symbols\":[\"CN\",\"US\",\"JP\"]}",
                country.toSchema().toString(false));

        AvroRecord combinedPrimitiveClass = AvroSchemaLoader.parseClass(CombinedPrimitiveClass.class);
        assertEquals(CombinedPrimitiveClass.class, combinedPrimitiveClass.getClazz());
        assertEquals("{\"type\":\"record\",\"name\":\"CombinedPrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"recordSet\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":\"int\"}}},{\"name\":\"listOfList\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"string\"}}},{\"name\":\"map2List\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"array\",\"items\":\"string\"}},\"null\"]}]}",
                combinedPrimitiveClass.toSchema().toString(false));

    }

    @Test
    public void testLoad() throws ClassNotFoundException {
        AvroSchemaLoader.load(ComplexClass.class, TypedClass.class, PrimitiveClass.class, Country.class, CombinedPrimitiveClass.class);
        assertEquals(5, AvroSchemaLoader.records.size());
        assertEquals(9, AvroSchemaLoader.schemas.size());
        assertEquals("{\"type\":\"record\",\"name\":\"TypedClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"field\",\"type\":\"string\"}]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaLoaderTest$TypedClass").toString());
        assertEquals("{\"type\":\"record\",\"name\":\"ComplexClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"aRecord\",\"type\":{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"fields\":[{\"name\":\"aString\",\"type\":\"string\"},{\"name\":\"aByte\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"aFloat\",\"type\":\"float\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"stringSet\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"integerList\",\"type\":{\"type\":\"array\",\"items\":\"int\"}},{\"name\":\"stringMap\",\"type\":{\"type\":\"map\",\"values\":\"double\"}},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":5}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimestamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}}]}},{\"name\":\"aRecordList\",\"type\":{\"type\":\"array\",\"items\":\"PrimitiveClass\"}},{\"name\":\"aRecordMap\",\"type\":[{\"type\":\"map\",\"values\":\"PrimitiveClass\"},\"null\"]},{\"name\":\"aRecordMap2List\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"map\",\"values\":\"PrimitiveClass\"}},\"null\"]},{\"name\":\"aTypedRecord\",\"type\":{\"type\":\"record\",\"name\":\"TypedClass\",\"fields\":[{\"name\":\"field\",\"type\":\"string\"}]}},{\"name\":\"countryEnum\",\"type\":{\"type\":\"enum\",\"name\":\"Country\",\"symbols\":[\"CN\",\"US\",\"JP\"]}},{\"name\":\"aBigDecimalList\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":5}}},{\"name\":\"aBigDecimalMapList\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":5}}},\"null\"]},{\"name\":\"aDateMap\",\"type\":{\"type\":\"map\",\"values\":{\"type\":\"int\",\"logicalType\":\"date\"}}},{\"name\":\"aString\",\"type\":\"string\"},{\"name\":\"aByte\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"aFloat\",\"type\":\"float\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"stringSet\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"integerList\",\"type\":{\"type\":\"array\",\"items\":\"int\"}},{\"name\":\"stringMap\",\"type\":{\"type\":\"map\",\"values\":\"double\"}},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":5}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimestamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}}]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaLoaderTest$ComplexClass").toString());
        assertEquals("{\"type\":\"record\",\"name\":\"PrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"aString\",\"type\":\"string\"},{\"name\":\"aByte\",\"type\":[\"bytes\",\"null\"]},{\"name\":\"aInt\",\"type\":\"int\"},{\"name\":\"aLong\",\"type\":[\"long\",\"null\"]},{\"name\":\"aFloat\",\"type\":\"float\"},{\"name\":\"aDouble\",\"type\":[\"double\",\"null\"]},{\"name\":\"aBoolean\",\"type\":[\"boolean\",\"null\"]},{\"name\":\"stringSet\",\"type\":[{\"type\":\"array\",\"items\":\"string\"},\"null\"]},{\"name\":\"integerList\",\"type\":{\"type\":\"array\",\"items\":\"int\"}},{\"name\":\"stringMap\",\"type\":{\"type\":\"map\",\"values\":\"double\"}},{\"name\":\"aBigDecimal\",\"type\":{\"type\":\"bytes\",\"logicalType\":\"decimal\",\"precision\":10,\"scale\":5}},{\"name\":\"aUuid\",\"type\":{\"type\":\"string\",\"logicalType\":\"uuid\"}},{\"name\":\"aDate\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}},{\"name\":\"aTimestamp\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}}]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaLoaderTest$PrimitiveClass").toString());
        assertEquals("{\"type\":\"enum\",\"name\":\"Country\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"symbols\":[\"CN\",\"US\",\"JP\"]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaLoaderTest$Country").toString());
        assertEquals("{\"type\":\"record\",\"name\":\"CombinedPrimitiveClass\",\"namespace\":\"com.nobodyhub.datalayer.core\",\"fields\":[{\"name\":\"recordSet\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"map\",\"values\":\"int\"}}},{\"name\":\"listOfList\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"array\",\"items\":\"string\"}}},{\"name\":\"map2List\",\"type\":[{\"type\":\"map\",\"values\":{\"type\":\"array\",\"items\":\"string\"}},\"null\"]}]}",
                AvroSchemaLoader.schemas.get("com.nobodyhub.datalayer.core.AvroSchemaLoaderTest$CombinedPrimitiveClass").toString());

    }

    private static class ComplexClass extends PrimitiveClass {
        @Column(nullable = false)
        private PrimitiveClass aRecord;
        @Column(nullable = false)
        private List<PrimitiveClass> aRecordList;
        @Column
        private Map<String, PrimitiveClass> aRecordMap;
        @Column
        private Map<String, List<PrimitiveClass>> aRecordMap2List;
        @Column
        private TypedClass<String> aTypedRecord;
        @Column(nullable = false)
        private Country countryEnum;
        @Column(nullable = false)
        private List<BigDecimal> aBigDecimalList;
        @Column
        private List<Map<String, BigDecimal>> aBigDecimalMapList;
        @Column(nullable = false)
        private Map<String, Date> aDateMap;
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