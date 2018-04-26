package com.nobodyhub.datalayer.core.service.repository.criteria;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import lombok.Data;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Data
public class Criteria implements AvroEntity {
    private int maxResult = -1;
    private RestrictionSet restrictionSet;
    private List<Order> orders;
    private List<Projection> projections;

    public void add(Order order) {
        if (orders == null) {
            orders = Lists.newArrayList();
        }
        orders.add(order);
    }

    public void add(Projection projection) {
        if (projections == null) {
            projections = Lists.newArrayList();
        }
        projections.add(projection);
    }
}