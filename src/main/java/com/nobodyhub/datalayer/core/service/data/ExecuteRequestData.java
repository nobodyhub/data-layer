package com.nobodyhub.datalayer.core.service.data;

import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import lombok.Builder;
import lombok.Data;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Data
@Builder
public class ExecuteRequestData<T> {
    private DataLayerProtocol.OpType opType;
    private T entity;

    public static <T> ExecuteRequestData<T> create(T entity) {
        return ExecuteRequestData.<T>builder()
                .opType(DataLayerProtocol.OpType.CREATE)
                .entity(entity)
                .build();
    }
}