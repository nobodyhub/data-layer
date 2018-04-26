package com.nobodyhub.datalayer.core.service;

import com.nobodyhub.datalayer.core.proto.DataLayerClientService;
import com.nobodyhub.datalayer.core.service.data.ExecuteRequestData;
import com.nobodyhub.datalayer.core.service.data.QueryRequestData;
import com.nobodyhub.datalayer.core.service.data.ResponseData;
import com.nobodyhub.datalayer.core.service.exception.DataLayerCoreException;
import com.nobodyhub.datalayer.core.service.repository.criteria.Restriction;
import com.nobodyhub.datalayer.core.service.repository.criteria.RestrictionSet;
import com.nobodyhub.datalayer.core.service.repository.criteria.RestrictionSetType;
import com.nobodyhub.datalayer.core.service.util.EntityHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author yan_h
 * @since 2018-04-26.
 */
public class DataLayerClient {
    private final DataLayerClientService dataLayerClientService;

    public DataLayerClient(String host, int port) {
        this.dataLayerClientService = new DataLayerClientService(host, port);
    }

    public <T> List<T> findAll(Class<T> cls) {
        QueryRequestData<T> requestData = new QueryRequestData<>(cls);
        ResponseData<List<T>> responseData = this.dataLayerClientService.query(requestData);
        return handleResult(responseData);
    }

    public <T> List<T> findByField(Class<T> cls, String fieldName, Object value) {
        QueryRequestData<T> requestData = new QueryRequestData<>(cls);
        RestrictionSet restrictionSet = new RestrictionSet(RestrictionSetType.CONJUNCTION);
        restrictionSet.addRestrictions(Restriction.eq(fieldName, value));
        requestData.setRestriction(restrictionSet);
        ResponseData<List<T>> responseData = this.dataLayerClientService.query(requestData);
        return handleResult(responseData);
    }

    public <T> T findById(Class<T> cls, Object value) {
        List<T> rst = findByField(cls, EntityHelper.PLACEHOLDER_ID, value);
        if (rst == null || rst.isEmpty()) {
            return null;
        } else {
            return rst.get(0);
        }
    }

    public <T> T execute(ExecuteRequestData<T>... requests) throws IOException, InterruptedException {
        ResponseData<T> responseData = this.dataLayerClientService.execute(requests);
        return handleResult(responseData);
    }

    protected <T> T handleResult(ResponseData<T> responseData) {
        switch (responseData.getStatus()) {
            case OK: {
                return responseData.getEntity();
            }
            case ERROR: {
                throw new DataLayerCoreException(responseData.getMessage());
            }
            default: {
                throw new DataLayerCoreException("Unknown Status Code: " + responseData.getStatus());
            }
        }
    }
}
