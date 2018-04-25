package com.nobodyhub.datalayer.core.service.repository.criteria;

/**
 * Projections that supported by Hibernate Projection
 *
 * @author yan_h
 * @see org.hibernate.criterion.Projections
 * @since 2018-04-24.
 */
public enum ProjectionType {
    GROUP_PROPERTY,
    ROW_COUNT,
    COUNT,
    COUNT_DISTINCT,
    MAX,
    MIN,
    AVG,
    SUM
}
