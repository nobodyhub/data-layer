package com.nobodyhub.datalayer.core.proto;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author yan_h
 * @see <a href="https://gist.github.com/nandub/950285">ProtobufEnvelope</a>
 * @since 2018-04-27.
 */
public class ProtoData {

    protected final LoadingCache<Class<?>, Descriptors.Descriptor> protoCache =
            CacheBuilder.newBuilder()
                    .weakKeys()
                    .build(new CacheLoader<Class<?>, Descriptors.Descriptor>() {
                        @Override
                        public Descriptors.Descriptor load(Class<?> key) throws Exception {
                            return getMessegeDescriptor(key);
                        }
                    });

    protected final LoadingCache<Class<?>, List<Field>> fieldCache =
            CacheBuilder.newBuilder()
            .weakKeys()
            .build(new CacheLoader<Class<?>, List<Field>>() {
                @Override
                public List<Field> load(Class<?> key) throws Exception {
                    return getFields(key);
                }
            });

    protected DescriptorProtos.DescriptorProto createProto(Class<?> cls) throws ExecutionException {
        DescriptorProtos.DescriptorProto.Builder protoBuilder = DescriptorProtos.DescriptorProto.newBuilder();
        protoBuilder.setName(cls.getSimpleName());
        List<Field> fields = fieldCache.get(cls);
        int idx = 1;
        for (Field field : fields) {
            DescriptorProtos.FieldDescriptorProto protoField =
                    DescriptorProtos.FieldDescriptorProto.newBuilder()
                            .setName(field.getName()).setNumber(idx++).setType(getType(field.getType())).build();
            protoBuilder.addField(protoField);
        }

        return protoBuilder.build();
    }

    protected List<Field> getFields(Class<?> cls) {
        List<Field> fields = Lists.newArrayList();
        if (cls.isEnum()) {

        } else {
            for (Field field : cls.getDeclaredFields()) {
                //TODO: add inherited field
                field.setAccessible(true);
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

    protected <T> DynamicMessage encode(T instance) throws ExecutionException, Descriptors.DescriptorValidationException, IllegalAccessException {
        Descriptors.Descriptor msgDescriptor = protoCache.get(instance.getClass());
        DynamicMessage.Builder dmBuilder = DynamicMessage.newBuilder(msgDescriptor);
        for (Field field : fieldCache.get(instance.getClass())) {
            dmBuilder.setField(msgDescriptor.findFieldByName(field.getName()), field.get(instance));
        }
        return dmBuilder.build();
    }

    protected Descriptors.Descriptor getMessegeDescriptor(Class<?> cls) throws ExecutionException, Descriptors.DescriptorValidationException {
        DescriptorProtos.DescriptorProto proto = createProto(cls);
        DescriptorProtos.FileDescriptorProto fileDescP = DescriptorProtos.FileDescriptorProto.newBuilder()
                .addMessageType(proto)
                .setPackage(cls.getPackage().getName())
                .build();

        Descriptors.FileDescriptor dynamicDescriptor = Descriptors.FileDescriptor.buildFrom(fileDescP, new Descriptors.FileDescriptor[0]);

        return dynamicDescriptor.findMessageTypeByName(cls.getSimpleName());
    }

    protected <T> void decode(T defaultInstance, ByteString data) throws ExecutionException, Descriptors.DescriptorValidationException, InvalidProtocolBufferException, IllegalAccessException, InstantiationException {
        Descriptors.Descriptor msgDescriptor = protoCache.get(defaultInstance.getClass());
        DynamicMessage.Builder dmBuilder = DynamicMessage.newBuilder(msgDescriptor);
        DynamicMessage msg = dmBuilder.build().getParserForType().parseFrom(data);
        Map<String, Field> fields = Maps.newHashMap();
        for (Field field : fieldCache.get(defaultInstance.getClass())) {
            fields.put(field.getName(), field);
        }

        for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : msg.getAllFields().entrySet()) {
            Descriptors.FieldDescriptor fieldDesp = entry.getKey();
            Field field = fields.get(fieldDesp.getName());
            field.set(defaultInstance, entry.getValue());
        }
    }

    /**
     * @param type
     * @return
     * @see <a href="https://developers.google.com/protocol-buffers/docs/proto3">Scalar Value Types</a>
     */
    protected DescriptorProtos.FieldDescriptorProto.Type getType(Class<?> type) {
        //TODO: add for all types
        if (type == String.class) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING;
        } else if (type == Integer.class || type == Integer.TYPE) {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32;
        } else {
            return DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING;
        }
    }
}
