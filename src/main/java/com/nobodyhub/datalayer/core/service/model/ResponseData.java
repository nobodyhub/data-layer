package com.nobodyhub.datalayer.core.service.model;

import lombok.Data;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Data
public class ResponseData<T> {
    private DataLayerProtocol.StatusCode errCode;
    private String message;
    private T entity;

    public boolean hasError() {
        return !DataLayerProtocol.StatusCode.OK.equals(errCode);
    }
}