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
        AvroRecord record = AvroSchemaConverter.parseClass(PrimitiveClass.class);
        assertEquals(PrimitiveClass.class, record.getClazz());
        record.toSchema();
    }

    static class ComplexClass extends PrimitiveClass {
        private PrimitiveClass aRecord;
        private TypedClass<String> aTypedRecord;
        private Country countryEnum;
    }

    static class TypedClass<T> {
        private T field;
    }

    static class PrimitiveClass {
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

    static enum Country {
        CN,
        US,
        JP
    }


}