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
    protected static String aString;
    @Column
    protected static ByteBuffer aByteBuffer;
    @Column(nullable = false)
    protected static int aInt;
    @Column
    protected static Integer aInteger;
    @Column
    protected static long along;
    @Column
    protected static Long aLong;
    @Column
    protected static float afloat;
    @Column
    protected static Float aFloat;
    @Column(nullable = false)
    protected static double adouble;
    @Column
    protected static Double aDouble;
    @Column
    protected static boolean aboolean;
    @Column(nullable = false)
    protected static Boolean aBoolean;
    @Column(precision = 10)
    protected static BigDecimal aBigDecimal;
    @Column(nullable = false)
    protected static Date aDate;
    @Column
    protected static Timestamp aTimeStamp;
    @Column(nullable = false)
    protected static UUID aUuid;

}
