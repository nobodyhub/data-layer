package com.nobodyhub.datalayer.core.cases;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Class contains primitive types in {@link org.apache.avro.Schema.Type}, excluding:
 * - {@link org.apache.avro.Schema.Type#UNION}
 * - {@link org.apache.avro.Schema.Type#FIXED}
 * - {@link org.apache.avro.Schema.Type#NULL}
 * <p>
 * includes both primitive types(like int) and boxing types(like Integer).
 *
 * @author yan_h
 * @since 2018-04-20.
 */
public class PrimitiveClass {
    @Column
    protected String aString;
    @Column
    protected ByteBuffer aByteBuffer;
    @Column(nullable = false)
    protected int aInt;
    @Column
    protected Integer aInteger;
    @Column
    protected long along;
    @Column
    protected Long aLong;
    @Column
    protected float afloat;
    @Column
    protected Float aFloat;
    @Column(nullable = false)
    protected double adouble;
    @Column
    protected Double aDouble;
    @Column
    protected boolean aboolean;
    @Column(nullable = false)
    protected Boolean aBoolean;
    @Column(precision = 10)
    protected BigDecimal aBigDecimal;
    @Column(nullable = false)
    protected Date aDate;
    @Column
    protected Timestamp aTimeStamp;
    @Column(nullable = false)
    protected UUID aUuid;

}
