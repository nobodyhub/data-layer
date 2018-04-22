package com.nobodyhub.datalayer.core.entity.cases;

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
    private List<String> aStringList;
    @Column(nullable = false)
    private List<ByteBuffer> aByteBufferList;
    @Column
    private ArrayList<Boolean> aBooleanList;
    @Column
    private LinkedList<Float> aFloatList;
    @Column
    private Map<String, Integer> aIntegerMap;
    @Column
    private Map<String, Long> aLongMap;
    @Column
    private HashMap<String, Double> aDoubleMap;
    @Column
    private TreeMap<String, BigDecimal> aBigDecimalMap;
    @Column
    private Set<UUID> aUuidSet;
    @Column
    private HashSet<Date> aDateSet;
    @Column
    private TreeSet<Timestamp> aTimestampSet;
}
