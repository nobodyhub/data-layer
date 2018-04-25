package com.nobodyhub.datalayer.core.service.model;

import lombok.Data;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Data
public class ExecuteRequestData<T> {
    private DataLayerProtocol.OpType opType;
    private T entity;
}