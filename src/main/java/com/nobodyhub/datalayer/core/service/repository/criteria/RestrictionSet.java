package com.nobodyhub.datalayer.core.service.repository.criteria;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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

    public void addRestrictions(Restriction... restriction) {
        restrictions.add(Sets.newHashSet(restriction));
    }
}
