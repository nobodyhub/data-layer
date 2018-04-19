package com.nobodyhub.datalayer.core;

import com.google.common.collect.Lists;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.specific.SpecificData;

import javax.persistence.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Convert a Hibernate entity to avro schema file
 * <p>
 * Reference:
 * - {@link SpecificData#createSchema}
 * - {@link ReflectData#createSchema(Type, Map)}
 * <p>
 * TODO: scan all classed in current classloader/specified package
 *
 * @author Ryan
 */
public final class AvroSchemaConverter {
    private AvroSchemaConverter() {
    }

    public static String parse(Class<?>... classes) {
        AvroSchema schema = new AvroSchema();
        for(Class<?> clazz: classes) {
            schema.addRecord(parseClass(clazz));
        }
        return schema.toString();
    }

    protected static AvroRecord parseClass(Class<?> clazz) {
        AvroRecord record = new AvroRecord(clazz);
        record.setNamespace(clazz.getPackage().getName());
        fillFieldInfo(record);
        return record;
    }

    protected static void fillFieldInfo(AvroRecord record) {
        Class<?> clz = record.getClazz();
        while (clz != null) {

            for (Field field : clz.getDeclaredFields()) {
                Annotation[] annotations = field.getAnnotationsByType(Column.class);
                if (annotations != null && annotations.length > 0) {

                }
            }

            clz = clz.getSuperclass();
        }
    }
}
