package com.nobodyhub.datalayer.core.service.repository;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.exception.DataLayerCoreException;
import com.nobodyhub.datalayer.core.service.repository.criteria.Criteria;
import com.nobodyhub.datalayer.core.service.util.AvroSchemaConverter;
import lombok.Getter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
public class DataService {
    private final DataRepository repository;
    @Getter
    private final SessionFactory sessionFactory;

    public DataService(SessionFactory sessionFactory) {
        this.repository = new DataRepository(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public List<DataLayerProtocol.Entity> query(Class entityClass, DataLayerProtocol.Entity entity) throws ClassNotFoundException, IOException {
        Criteria criteria = AvroSchemaConverter.decode(entity);
        List<DataLayerProtocol.Entity> entityList = Lists.newArrayList();
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List rstList = repository.query(session, entityClass,
                    criteria.getMaxResult(),
                    criteria.getRestrictionSet(),
                    criteria.getOrders(),
                    criteria.getProjections());
            for (Object ele : rstList) {
                entityList.add(AvroSchemaConverter.encode(ele));
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return entityList;
    }

    public DataLayerProtocol.Entity execute(List<DataLayerProtocol.ExecuteRequest> operations) throws IOException, ClassNotFoundException {
        //stores the result of the last operation
        Object obj = null;
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            for (DataLayerProtocol.ExecuteRequest request : operations) {
                switch (request.getOpType()) {
                    case CREATE: {
                        obj = repository.create(session, AvroSchemaConverter.decode(request.getEntity()));
                        break;
                    }
                    case UPDATE: {
                        obj = repository.update(session, AvroSchemaConverter.decode(request.getEntity()));
                        break;
                    }
                    case DELETE: {
                        obj = repository.delete(session, AvroSchemaConverter.decode(request.getEntity()));
                        break;
                    }
                    case PERSIST: {
                        obj = repository.persist(session, AvroSchemaConverter.decode(request.getEntity()));
                        break;
                    }
                    default: {
                        throw new DataLayerCoreException("Unknown OpType:" + request.getOpType());
                    }
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
        return AvroSchemaConverter.encode(obj);
    }
}
