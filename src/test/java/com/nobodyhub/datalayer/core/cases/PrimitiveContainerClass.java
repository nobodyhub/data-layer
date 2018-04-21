package com.nobodyhub.datalayer.core.cases;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * Class includes shallow container of primitive types
 *
 * @author yan_h
 * @since 2018-04-20.
 */
public class PrimitiveContainerClass {
    @Column
    private static List<String> aStringList;
    @Column(nullable = false)
    private static List<ByteBuffer> aByteBufferList;
    @Column
    private static ArrayList<Boolean> aBooleanList;
    @Column
    private static LinkedList<Float> aFloatList;
    @Column
    private static Map<String, Integer> aIntegerMap;
    @Column
    private static Map<String, Long> aLongMap;
    @Column
    private static HashMap<String, Double> aDoubleMap;
    @Column
    private static TreeMap<String, BigDecimal> aBigDecimalMap;
    @Column
    private static Set<UUID> aUuidSet;
    @Column
    private static HashSet<Date> aDateSet;
    @Column
    private static TreeSet<Timestamp> aTimestampSet;
}
