package com.nobodyhub.datalayer.core.db;

import com.nobodyhub.datalayer.core.db.criteria.Projection;
import com.nobodyhub.datalayer.core.db.criteria.RestrictionSet;
import lombok.RequiredArgsConstructor;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import java.util.List;

/**
 * @author Ryan
 */
@RequiredArgsConstructor
public class EntityRepository {

    private final SessionFactory sessionFactory;

    /**
     * Query the result of type <code>entityClass</code> by given criteria
     *
     * @param entityClass  the Class of the entity
     * @param nMaxResults  max results to be returned
     * @param restrictions restrictions for query
     * @param orders       order clause, created by {@link Order#asc(String)} or {@link Order#desc(String)}
     * @param projections  projections, aggregation and grouping, created by {@link Projections#projectionList()}
     * @param <T>
     * @return
     * @see <a href="http://docs.jboss.org/hibernate/core/3.5/reference/en/html/querycriteria.html#querycriteria-projection">Criteria Queries</a>
     * @see <a href="https://en.wikipedia.org/wiki/Disjunctive_normal_form">DNF</a>
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> query(Class<T> entityClass, int nMaxResults, RestrictionSet restrictions, List<Order> orders, List<Projection> projections) {
        //TODO: use getCriteriaBuilder
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(entityClass);
        // max results
        if (nMaxResults > 0) {
            criteria.setMaxResults(nMaxResults);
        }
        //restriction
        if (restrictions != null) {
            restrictions.toCriteria(criteria);
        }
        if (projections != null && !projections.isEmpty()) {
            ProjectionList projList = Projections.projectionList();
            for (Projection proj : projections) {
                proj.addToProjectionList(projList);
            }
            criteria.setProjection(projList);
        }
        //order
        if (orders != null && !orders.isEmpty()) {
            for (Order order : orders) {
                criteria.addOrder(order);
            }
        }
        return criteria.list();
    }

    /**
     * Update entity
     *
     * @param entity
     * @param <T>
     */
    public <T> void update(T entity) {
        sessionFactory.getCurrentSession().update(entity);
    }

    /**
     * Delete entity
     *
     * @param entity
     * @param <T>
     */
    public <T> void remove(T entity) {
        sessionFactory.getCurrentSession().remove(entity);
    }

    /**
     * Create entity
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T create(T entity) {
        sessionFactory.getCurrentSession().save(entity);
        return entity;
    }

    /**
     * Create or Update entity
     *
     * @param entity
     * @param <T>
     */
    public <T> void persist(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }
}
