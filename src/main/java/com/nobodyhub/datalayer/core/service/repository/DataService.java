package com.nobodyhub.datalayer.core.service.repository;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.exception.AvroCoreException;
import com.nobodyhub.datalayer.core.service.repository.criteria.Criteria;
import com.nobodyhub.datalayer.core.service.util.AvroSchemaConverter;

import java.io.IOException;
import java.util.List;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
public class DataService {
    private DataRepository repository;
    private AvroSchemaConverter converter;

    public List<DataLayerProtocol.Entity> query(Class entityClass, DataLayerProtocol.Entity entity) throws ClassNotFoundException, IOException {
        Criteria criteria = converter.decode(entity);
        List rstList = repository.query(entityClass,
                criteria.getMaxResult(),
                criteria.getRestrictionSet(),
                criteria.getOrders(),
                criteria.getProjections());

        List<DataLayerProtocol.Entity> entityList = Lists.newArrayList();
        for (Object ele : rstList) {
            entityList.add(converter.encode(ele));
        }
        return entityList;
    }

    public DataLayerProtocol.Entity execute(List<DataLayerProtocol.ExecuteRequest> operations) throws IOException, ClassNotFoundException {
        //stores the result of the last operation
        Object obj = null;
        for (DataLayerProtocol.ExecuteRequest request : operations) {
            switch (request.getOpType()) {
                case CREATE: {
                    obj = repository.create(converter.decode(request.getEntity()));
                    break;
                }
                case UPDATE: {
                    obj = repository.create(converter.decode(request.getEntity()));
                    break;
                }
                case DELETE: {
                    obj = repository.create(converter.decode(request.getEntity()));
                    break;
                }
                case PERSIST: {
                    obj = repository.persist(converter.decode(request.getEntity()));
                    break;
                }
                default: {
                    throw new AvroCoreException("Unknown OpType:" + request.getOpType());
                }
            }
        }
        return converter.encode(obj);
    }
}
