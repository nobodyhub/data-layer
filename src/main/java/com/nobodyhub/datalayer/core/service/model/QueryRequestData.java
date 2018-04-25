package com.nobodyhub.datalayer.core.service.model;

import com.nobodyhub.datalayer.core.db.criteria.Criteria;
import lombok.Data;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Data
public class QueryRequestData<T> {
    private Class<T> cls;
    private Criteria criteria;
}