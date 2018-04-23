package com.nobodyhub.datalayer.core.entity.data;

import org.apache.avro.Conversions;
import org.apache.avro.Schema;
import org.apache.avro.data.TimeConversions;
import org.apache.avro.reflect.*;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Ryan
 */
public class AvroData extends ReflectData {
    private static final AvroData INSTANCE = new AvroData();

    /**
     * For subclasses.  Applications normally use {@link AvroData#get()}.
     */
    public AvroData() {
        super();
    }

    /**
     * Construct with a particular classloader.
     */
    public AvroData(ClassLoader classLoader) {
        super(classLoader);
    }

    /**
     * Return the singleton instance.
     */
    public static AvroData get() {
        return INSTANCE;
    }

    /**
     * change the precision&scale and nullable according to the {@link Column} annotation on field
     *
     * @param field
     * @param names
     * @return
     */
    @Override
    protected Schema createFieldSchema(Field field, Map<String, Schema> names) {
        Column columnAnno = field.getAnnotation(Column.class);

        if (field.isAnnotationPresent(AvroEncode.class)
                || field.isAnnotationPresent(AvroSchema.class)) {
            return super.createFieldSchema(field, names);
        }

        int precision = columnAnno == null || columnAnno.precision() == 0 ? 19 : columnAnno.precision();
        int scale = columnAnno == null || columnAnno.scale() == 0 ? 2 : columnAnno.scale();


        //add conversions
        addLogicalTypeConversion(new BigDecimalConversion(precision, scale));
        addLogicalTypeConversion(new Conversions.UUIDConversion());
        addLogicalTypeConversion(new TimeConversions.DateConversion());
        addLogicalTypeConversion(new TimeConversions.TimestampConversion());

        Schema schema = createSchema(field.getGenericType(), names);

        if (field.isAnnotationPresent(Stringable.class)) {
            schema = super.createFieldSchema(field, names);
        }

        boolean isNullable = field.isAnnotationPresent(Nullable.class) || (columnAnno == null || columnAnno.nullable());
        if (isNullable) {
            schema = makeNullable(schema);
        }


        return schema;
    }
}
