package com.nobodyhub.datalayer.core.db;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;

/**
 * @author Ryan
 */
@RequiredArgsConstructor
public class EntityRepository {

    private final SessionFactory sessionFactory;

    /**
     * Query the result of type <code>entityClass</code> by given criteria
     *
     * @param entityClass
     * @param condition
     * @param <T>
     * @return
     */
    public <T> List<T> query(Class<T> entityClass, Map<String, String> condition) {
        //TODO: add more criteria
        CriteriaQuery<T> query = sessionFactory.getCurrentSession().getCriteriaBuilder()
                .createQuery(entityClass);
        return sessionFactory.getCurrentSession().createQuery(query).getResultList();
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
