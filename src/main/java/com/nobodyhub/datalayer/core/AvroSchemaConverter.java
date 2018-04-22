package com.nobodyhub.datalayer.core;

import com.google.protobuf.ByteString;
import com.nobodyhub.datalayer.core.model.DataLayerProtocol;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Ryan
 */
@RequiredArgsConstructor
public class AvroSchemaConverter {
    private final AvroSchemaLoader loader;

    public <T extends AvroEntity> DataLayerProtocol.Entity from(T avroEntity) throws ClassNotFoundException, IOException {
        Class clz = avroEntity.getClass();
        String qualifiedClassName = clz.getName();
        Schema schema = ReflectData.get().getSchema(Class.forName(qualifiedClassName));

        ReflectDatumWriter<T> datumWriter = new ReflectDatumWriter<>(schema);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        datumWriter.write(avroEntity, EncoderFactory.get().directBinaryEncoder(outputStream, null));

        return DataLayerProtocol.Entity.newBuilder()
                .setEntityClass(qualifiedClassName)
                .setEntity(ByteString.copyFrom(outputStream.toByteArray()))
                .build();
    }

    public <T extends AvroEntity> T to(DataLayerProtocol.Entity entity) throws ClassNotFoundException, IOException {

        String qualifiedClassName = entity.getEntityClass();
//        Schema schema = loader.getSchema(qualifiedClassName);
        Schema reflectSchema = ReflectData.get().getSchema(Class.forName(qualifiedClassName));
        ReflectDatumReader<T> datumReflectReader = new ReflectDatumReader<>(reflectSchema);
        return datumReflectReader.read(null, DecoderFactory.get().binaryDecoder(entity.getEntity().toByteArray(), null));
    }
}
