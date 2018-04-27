package com.nobodyhub.datalayer.core.service.data;

import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import com.nobodyhub.datalayer.core.service.repository.criteria.Criteria;
import lombok.Builder;
import lombok.Data;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Data
@Builder
public class RequestData<T> {
    private DataLayerProtocol.OpType opType;
    private Class<T> cls;
    private T entity;
    private Criteria criteria;

    public static <T> RequestData<T> find(Class<T> cls) {
        return RequestData.<T>builder()
                .opType(DataLayerProtocol.OpType.READ)
                .cls(cls)
                .build();
    }

    public static <T> RequestData<T> find(Class<T> cls, Criteria criteria) {
        return RequestData.<T>builder()
                .opType(DataLayerProtocol.OpType.READ)
                .cls(cls)
                .criteria(criteria)
                .build();
    }

    public static <T> RequestData<T> create(T entity) {
        return RequestData.<T>builder()
                .opType(DataLayerProtocol.OpType.CREATE)
                .entity(entity)
                .build();
    }

    public static <T> RequestData<T> update(T entity) {
        return RequestData.<T>builder()
                .opType(DataLayerProtocol.OpType.UPDATE)
                .entity(entity)
                .build();
    }

    public static <T> RequestData<T> delete(T entity) {
        return RequestData.<T>builder()
                .opType(DataLayerProtocol.OpType.DELETE)
                .entity(entity)
                .build();
    }

    public static <T> RequestData<T> persist(T entity) {
        return RequestData.<T>builder()
                .opType(DataLayerProtocol.OpType.PERSIST)
                .entity(entity)
                .build();
    }
}