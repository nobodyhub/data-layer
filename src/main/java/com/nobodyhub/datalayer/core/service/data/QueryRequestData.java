package com.nobodyhub.datalayer.core.service.data;

import com.nobodyhub.datalayer.core.service.repository.criteria.Criteria;
import com.nobodyhub.datalayer.core.service.repository.criteria.Projection;
import com.nobodyhub.datalayer.core.service.repository.criteria.RestrictionSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Getter
@RequiredArgsConstructor
public class QueryRequestData<T> {
    private final Class<T> cls;
    private Criteria criteria = new Criteria();

    public void setRestriction(RestrictionSet restriction) {
        criteria.setRestrictionSet(restriction);
    }

    public void addOrder(Order order) {
        criteria.add(order);
    }

    public void addProjection(Projection projection) {
        criteria.add(projection);
    }
}