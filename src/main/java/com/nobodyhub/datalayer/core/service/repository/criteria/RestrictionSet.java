package com.nobodyhub.datalayer.core.service.repository.criteria;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import com.nobodyhub.datalayer.core.service.exception.AvroCoreException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Set;

/**
 * @author yan_h
 * @since 2018-04-23.
 */
@RequiredArgsConstructor
@Data
public class RestrictionSet implements AvroEntity {
    /**
     * the junction type of restriction set, decideing how to eval the restrictions
     */
    private final RestrictionSetType type;
    /**
     * Restrictions to be evaluated
     */
    private List<Set<Restriction>> restrictions = Lists.newArrayList();

    public void toCriteria(Criteria criteria) {
        switch (type) {
            case DISJUNCTION: {
                handleConjunction(criteria);
                break;
            }
            case CONJUNCTION: {
                handleDisjunction(criteria);
                break;
            }
            default: {
                throw new AvroCoreException("Unknown type:" + type);
            }
        }
    }

    /**
     * handle {@link this#restrictions} as a conjunction of conditions, which means:
     * (S11&&S12&&S13&&...)||(S21&&S22&&S23&&...)||(S31&&S32&&S33&&...)||...
     *
     * @param criteria
     */
    private void handleDisjunction(Criteria criteria) {
        Disjunction disjunction = Restrictions.disjunction();
        for (Set<Restriction> restrictionSet : restrictions) {
            Conjunction subConjunction = Restrictions.conjunction();
            for (Restriction restriction : restrictionSet) {
                subConjunction.add(restriction.toRestriction());
            }
            disjunction.add(subConjunction);
        }
        criteria.add(disjunction);
    }

    /**
     * handle {@link this#restrictions} as a conjunction of conditions, which means:
     * (S11||S12||S13||...)&&(S21||S22||S23||...)&&(S31||S32||S33||...)&&...
     *
     * @param criteria
     */
    protected void handleConjunction(Criteria criteria) {
        for (Set<Restriction> restrictionSet : restrictions) {
            Disjunction subDisjunction = Restrictions.disjunction();
            for (Restriction restriction : restrictionSet) {
                subDisjunction.add(restriction.toRestriction());
            }
            criteria.add(subDisjunction);
        }
    }
}
