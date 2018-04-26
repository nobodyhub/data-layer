package com.nobodyhub.datalayer.core.service.repository.criteria;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import com.nobodyhub.datalayer.core.service.exception.DataLayerCoreException;
import lombok.Builder;
import lombok.Data;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author yan_h
 * @since 2018-04-23.
 */
@Data
@Builder
public class Restriction implements AvroEntity {
    /**
     * type of restriction
     */
    private RestrictionOpType type;
    /**
     * the name of field to apply restrictions
     */
    private String fieldName;
    /**
     * a list of oprands, the meaning depends on the {@link this#type}
     */
    private List<Object> operands;
    /**
     * only for {@link RestrictionOpType#LIKE} or {@link RestrictionOpType#ILIKE}
     */
    private MatchMode matchMode;

    public Criterion toRestriction(Class fieldCls) {
        switch (type) {
            case EQ: {
                return Restrictions.eq(fieldName, fieldCls.cast(operands.get(0)));
            }
            case LIKE: {
                if (matchMode != null && fieldCls == String.class) {
                    return Restrictions.like(fieldName, String.class.cast(operands.get(0)), matchMode);
                } else {
                    return Restrictions.like(fieldName, fieldCls.cast(operands.get(0)));
                }
            }
            case BEWTEEN: {
                return Restrictions.between(fieldName, fieldCls.cast(operands.get(0)), fieldCls.cast(operands.get(1)));
            }
            case IN: {
                return Restrictions.in(fieldName, operands.toArray());
            }
            default: {
                //TODO: add all types
                throw new DataLayerCoreException("Not Implemented!");
            }
        }
    }

    public static Restriction eq(String fieldName, Object value) {
        return Restriction.builder()
                .type(RestrictionOpType.EQ)
                .fieldName(fieldName)
                .operands(Lists.newArrayList(value))
                .build();
    }

    public static Restriction like(String fieldName, Object value) {
        return Restriction.builder()
                .type(RestrictionOpType.LIKE)
                .fieldName(fieldName)
                .operands(Lists.newArrayList(value))
                .build();
    }

    public static Restriction like(String fieldName, Object value, MatchMode matchMode) {
        return Restriction.builder()
                .type(RestrictionOpType.LIKE)
                .fieldName(fieldName)
                .operands(Lists.newArrayList(value))
                .matchMode(matchMode)
                .build();
    }

    public static Restriction between(String fieldName, Object lower, Object upper) {
        return Restriction.builder()
                .type(RestrictionOpType.BEWTEEN)
                .fieldName(fieldName)
                .operands(Lists.newArrayList(lower, upper))
                .build();
    }

    public static Restriction in(String fieldName, Object... values) {
        return Restriction.builder()
                .type(RestrictionOpType.BEWTEEN)
                .fieldName(fieldName)
                .operands(Lists.newArrayList(values))
                .build();
    }
}
