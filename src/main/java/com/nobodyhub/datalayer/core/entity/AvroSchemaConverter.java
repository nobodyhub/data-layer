package com.nobodyhub.datalayer.core.entity;

import com.google.protobuf.ByteString;
import com.nobodyhub.datalayer.core.entity.common.AvroEntity;
import com.nobodyhub.datalayer.core.entity.data.AvroData;
import com.nobodyhub.datalayer.core.entity.data.AvroDatumReader;
import com.nobodyhub.datalayer.core.entity.data.AvroDatumWriter;
import com.nobodyhub.datalayer.core.service.model.DataLayerProtocol;
import org.apache.avro.Schema;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Ryan
 */
@Component
public class AvroSchemaConverter {

    public <T> DataLayerProtocol.Entity from(T avroEntity) throws ClassNotFoundException, IOException {
        Class clz = avroEntity.getClass();
        String qualifiedClassName = clz.getName();
        Schema schema = AvroData.get().getSchema(clz);

        AvroDatumWriter<T> datumWriter = new AvroDatumWriter<>(schema);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        datumWriter.write(avroEntity, EncoderFactory.get().directBinaryEncoder(outputStream, null));

        return DataLayerProtocol.Entity.newBuilder()
                .setEntityClass(qualifiedClassName)
                .setEntity(ByteString.copyFrom(outputStream.toByteArray()))
                .build();
    }

    public <T> T to(DataLayerProtocol.Entity entity) throws ClassNotFoundException, IOException {
        String qualifiedClassName = entity.getEntityClass();
        Schema reflectSchema = AvroData.get().getSchema(Class.forName(qualifiedClassName));
        AvroDatumReader<T> datumReflectReader = new AvroDatumReader<>(reflectSchema);
        return datumReflectReader.read(null, DecoderFactory.get().binaryDecoder(entity.getEntity().toByteArray(), null));
    }
}
