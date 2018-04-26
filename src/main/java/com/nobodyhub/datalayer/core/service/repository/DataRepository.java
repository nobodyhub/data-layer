package com.nobodyhub.datalayer.core.service.repository;

import com.nobodyhub.datalayer.core.service.exception.DataLayerCoreException;
import com.nobodyhub.datalayer.core.service.repository.criteria.Projection;
import com.nobodyhub.datalayer.core.service.repository.criteria.Restriction;
import com.nobodyhub.datalayer.core.service.repository.criteria.RestrictionSet;
import com.nobodyhub.datalayer.core.service.util.EntityHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;

import java.util.List;
import java.util.Set;

/**
 * @author Ryan
 */
@RequiredArgsConstructor
public class DataRepository {
    @Getter
    private final SessionFactory sessionFactory;

    /**
     * Query the result of type <code>entityClass</code> by given criteria
     *
     * @param session        DB Session
     * @param entityClass    the Class of the entity
     * @param nMaxResults    max results to be returned
     * @param restrictionSet restrictions for query
     * @param orders         order clause, created by {@link Order#asc(String)} or {@link Order#desc(String)}
     * @param projections    projections, aggregation and grouping, created by {@link Projections#projectionList()}
     * @return
     * @see <a href="http://docs.jboss.org/hibernate/core/3.5/reference/en/html/querycriteria.html#querycriteria-projection">Criteria Queries</a>
     * @see <a href="https://en.wikipedia.org/wiki/Disjunctive_normal_form">DNF</a>
     */
    @SuppressWarnings("unchecked")
    public List query(Session session, Class entityClass, int nMaxResults, RestrictionSet restrictionSet, List<Order> orders, List<Projection> projections) {
        //TODO: use getCriteriaBuilder
        Criteria criteria = session.createCriteria(entityClass);
        // max results
        if (nMaxResults > 0) {
            criteria.setMaxResults(nMaxResults);
        }
        //restriction
        if (restrictionSet != null) {
            ClassMetadata meta = sessionFactory.getClassMetadata(entityClass);
            toCriteria(meta, restrictionSet, criteria);
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

    public void toCriteria(ClassMetadata meta, RestrictionSet restrictionSet, Criteria criteria) {
        switch (restrictionSet.getType()) {
            case DISJUNCTION: {
                handleConjunction(meta, restrictionSet, criteria);
                break;
            }
            case CONJUNCTION: {
                handleDisjunction(meta, restrictionSet, criteria);
                break;
            }
            default: {
                throw new DataLayerCoreException("Unknown type:" + restrictionSet.getType());
            }
        }
    }

    /**
     * handle {@link RestrictionSet#restrictions} as a conjunction of conditions, which means:
     * (S11&&S12&&S13&&...)||(S21&&S22&&S23&&...)||(S31&&S32&&S33&&...)||...
     *
     * @param criteria
     */
    private void handleDisjunction(ClassMetadata meta, RestrictionSet restrictionSets, Criteria criteria) {
        Disjunction disjunction = Restrictions.disjunction();
        for (Set<Restriction> restrictionSet : restrictionSets.getRestrictions()) {
            Conjunction subConjunction = Restrictions.conjunction();
            for (Restriction restriction : restrictionSet) {
                Class cls = EntityHelper.getFieldClass(meta, restriction.getFieldName());
                subConjunction.add(restriction.toRestriction(cls));
            }
            disjunction.add(subConjunction);
        }
        criteria.add(disjunction);
    }

    /**
     * handle {@link RestrictionSet#restrictions} as a conjunction of conditions, which means:
     * (S11||S12||S13||...)&&(S21||S22||S23||...)&&(S31||S32||S33||...)&&...
     *
     * @param criteria
     */
    protected void handleConjunction(ClassMetadata meta, RestrictionSet restrictionSets, Criteria criteria) {
        for (Set<Restriction> restrictionSet : restrictionSets.getRestrictions()) {
            Disjunction subDisjunction = Restrictions.disjunction();
            for (Restriction restriction : restrictionSet) {
                Class cls = EntityHelper.getFieldClass(meta, restriction.getFieldName());
                subDisjunction.add(restriction.toRestriction(cls));
            }
            criteria.add(subDisjunction);
        }
    }

    /**
     * Update entity
     *
     * @param entity
     * @param <T>
     */
    public <T> T update(Session session, T entity) {
        session.update(entity);
        return entity;
    }

    /**
     * Delete entity
     *
     * @param entity
     * @param <T>
     */
    public <T> T delete(Session session, T entity) {
        session.remove(entity);
        return entity;
    }

    /**
     * Create entity
     *
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T create(Session session, T entity) {
        session.save(entity);
        return entity;
    }

    /**
     * Create or Update entity
     *
     * @param entity
     * @param <T>
     */
    public <T> T persist(Session session, T entity) {
        session.saveOrUpdate(entity);
        return entity;
    }
}
