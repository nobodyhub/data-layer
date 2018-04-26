package com.nobodyhub.datalayer.core.service.util;

import com.google.protobuf.ByteString;
import com.nobodyhub.datalayer.core.avro.AvroData;
import com.nobodyhub.datalayer.core.avro.AvroDatumReader;
import com.nobodyhub.datalayer.core.avro.AvroDatumWriter;
import com.nobodyhub.datalayer.core.proto.DataLayerProtocol;
import org.apache.avro.Schema;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Ryan
 */
public class AvroSchemaConverter {

    public <T> DataLayerProtocol.Entity encode(T avroEntity) throws IOException {
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

    public <T> T decode(DataLayerProtocol.Entity entity) throws ClassNotFoundException, IOException {
        String qualifiedClassName = entity.getEntityClass();
        Schema reflectSchema = AvroData.get().getSchema(Class.forName(qualifiedClassName));
        AvroDatumReader<T> datumReflectReader = new AvroDatumReader<>(reflectSchema);
        return datumReflectReader.read(null, DecoderFactory.get().binaryDecoder(entity.getEntity().toByteArray(), null));
    }
}
