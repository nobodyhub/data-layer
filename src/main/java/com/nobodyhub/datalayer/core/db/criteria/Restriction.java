package com.nobodyhub.datalayer.core.db.criteria;

import com.nobodyhub.datalayer.core.exception.AvroCoreException;
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
public class Restriction {
    /**
     * type of restriction
     */
    private RestrictionOpType type;
    /**
     * the name of field to apply restrictions
     */
    private String fieldName;
    /**
     * the Type of field
     */
    private Class fieldCls;
    /**
     * a list of oprands, the meaning depends on the {@link this#type}
     */
    private List<Object> operands;
    /**
     * only for {@link RestrictionOpType#LIKE} or {@link RestrictionOpType#ILIKE}
     */
    private MatchMode matchMode;

    public Criterion toRestriction() {
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
                throw new AvroCoreException("Not Implemented!");
            }
        }
    }
}
