package com.nobodyhub.datalayer.core.db;

import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.entity.AvroSchemaConverter;
import com.nobodyhub.datalayer.core.entity.common.AvroVoidEntity;
import com.nobodyhub.datalayer.core.service.model.DataLayerProtocol;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

/**
 * @author Ryan
 */
@RequiredArgsConstructor
public class EntityTransaction {
    private final AvroSchemaConverter converter;
    private final List<DataLayerProtocol.ExecuteRequest> requests = Lists.newArrayList();

    public void addRequest(DataLayerProtocol.ExecuteRequest request) {
        requests.add(request);
    }

    public void clear() {
        requests.clear();
    }

    public void onError(Throwable t) {
        //TODO: handle the Throwable
        this.clear();
    }

    public DataLayerProtocol.Response execute() throws IOException, ClassNotFoundException {
        //TODO: use an actual entity returned by executing request
        DataLayerProtocol.Entity entity = converter.from(AvroVoidEntity.get());

        return DataLayerProtocol.Response.newBuilder()
                .setErrorCode(0)
                .setEntity(entity)
                .build();
    }
}