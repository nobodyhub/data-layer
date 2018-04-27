package com.nobodyhub.datalayer.core.service.repository.criteria;

import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import org.hibernate.criterion.Projections;

/**
 * Projections that supported by Hibernate Projection
 *
 * @author yan_h
 * @see org.hibernate.criterion.Projections
 * @since 2018-04-24.
 */
public enum ProjectionType implements AvroEntity {
    /**
     * @see Projections#groupProperty(String)
     */
    GROUP_PROPERTY,
    /**
     * @see Projections#rowCount()
     */
    ROW_COUNT,
    /**
     * @see Projections#count(String)
     */
    COUNT,
    /**
     * @see Projections#countDistinct(String)
     */
    COUNT_DISTINCT,
    /**
     * @see Projections#max(String)
     */
    MAX,
    /**
     * @see Projections#min(String)
     */
    MIN,
    /**
     * @see Projections#avg(String)
     */
    AVG,
    /**
     * @see Projections#sum(String)
     */
    SUM
}
