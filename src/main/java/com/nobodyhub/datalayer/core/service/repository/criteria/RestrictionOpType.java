package com.nobodyhub.datalayer.core.service.repository.criteria;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Type of restrictions
 *
 * @author yan_h
 * @see Restrictions
 * @since 2018-04-23.
 */
public enum RestrictionOpType {
    /**
     * Equal
     */
    EQ,
    /**
     * Equal or is null
     */
    EQ_OR_IS_NULL,
    /**
     * Not Equal
     */
    NE,
    /**
     * Not Equal or Is not null
     */
    NE_OR_IS_NOT_NULL,
    /**
     * Like
     *
     * @see MatchMode
     */
    LIKE,
    /**
     * A case-insensitive "like"
     */
    ILIKE,
    /**
     * Greater than
     */
    GT,
    /**
     * Greater than or Equal
     */
    GE,
    /**
     * Less than
     */
    LT,
    /**
     * Less than or Equal
     */
    LE,
    /**
     * Between
     */
    BEWTEEN,
    /**
     * In
     */
    IN,
    /**
     * is Null
     */
    IS_NULL,
    /**
     * is not Null
     */
    IS_NOT_NULL,
    /**
     * properties equal
     */
    EQ_PROP,
    /**
     * properties not equal
     */
    NE_PROP,
    /**
     * one property less than another
     */
    LT_PROP,
    /**
     * one property less than or equal another
     */
    LE_PROP,
    /**
     * one property greater than another
     */
    GT_PROP,
    /**
     * one property greater than or equal another
     */
    GE_PROP
}
