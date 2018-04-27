package com.nobodyhub.datalayer.core.proto;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author yan_h
 * @see <a href="https://gist.github.com/nandub/950285">ProtobufEnvelope</a>
 * @since 2018-04-27.
 */
public class ProtoData {

    private final LoadingCache<Class<?>, DescriptorProtos.DescriptorProto> protoCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, DescriptorProtos.DescriptorProto>() {
                        @Override
                        public DescriptorProtos.DescriptorProto load(Class<?> key) throws Exception {
                            return createProto(key);
                        }
                    });

    private DescriptorProtos.DescriptorProto createProto(Class<?> cls) {
        DescriptorProtos.DescriptorProto.Builder protoBuilder = DescriptorProtos.DescriptorProto.newBuilder();
        protoBuilder.setName(cls.getName());
        List<Field> fields = getFields(cls);
        int idx = 1;
        for (Field field : fields) {
            DescriptorProtos.FieldDescriptorProto protoField =
                    DescriptorProtos.FieldDescriptorProto.newBuilder()
                            .setName(field.getName()).setNumber(idx++).setType(getType(field.getType())).build();
            protoBuilder.addField(protoField);
        }

        return protoBuilder.build();
    }

    private List<Field> getFields(Class<?> cls) {
        List<Field> fields = Lists.newArrayList();
        if (cls.isEnum()) {

        } else {
            for (Field field : cls.getDeclaredFields()) {
                //TODO: add inherited field
                fields.add(field);
            }
            //because The elements returned by cls.getDeclaredFields()
            // are not sorted and are not in any particular order
            Collections.sort(fields, (o1, o2) -> (
                    o1.getName().compareTo(o2.getName())
            ));
        }
        return fields;
    }

    private <T> Message convert(T instance) throws ExecutionException, Descriptors.DescriptorValidationException, IllegalAccessException {
        DescriptorProtos.DescriptorProto proto = protoCache.get(instance.getClass());
        DescriptorProtos.FileDescriptorProto fileDescP = DescriptorProtos.FileDescriptorProto.newBuilder()
                .addMessageType(proto).build();

        Descriptors.FileDescriptor dynamicDescriptor = Descriptors.FileDescriptor.buildFrom(fileDescP, new Descriptors.FileDescriptor[0]);

        Descriptors.Descriptor msgDescriptor = dynamicDescriptor.findMessageTypeByName(instance.getClass().getName());
        DynamicMessage.Builder dmBuilder =
                DynamicMessage.newBuilder(msgDescriptor);
        for (Field field : getFields(instance.getClass())) {
            dmBuilder.setField(msgDescriptor.findFieldByName(field.getName()), field.get(instance));
        }
        return dmBuilder.build();
    }

    /**
     * @param type
     * @return
     * @see <a href="https://developers.google.com/protocol-buffers/docs/proto3">Scalar Value Types</a>
     */
    private DescriptorProtos.FieldDescriptorProto.Type getType(Class<?> type) {
        //TODO: add for all types
        if (type == Double.class || type == Double.TYPE) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
        } else {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES;
        }
    }
}
