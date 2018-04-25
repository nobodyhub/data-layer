package com.nobodyhub.datalayer.core.service.data;

import com.nobodyhub.datalayer.core.service.repository.criteria.Criteria;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
@Data
@RequiredArgsConstructor
public class QueryRequestData<T> {
    private final Class<T> cls;
    private Criteria criteria = new Criteria();
}