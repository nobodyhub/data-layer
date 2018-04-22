package com.nobodyhub.datalayer.core.annotation;

import com.nobodyhub.datalayer.core.AvroEntity;

import javax.persistence.Entity;
import java.lang.annotation.*;

/**
 * @author Ryan
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvroSchemaLoaderConfiguration {
    /**
     * the package to be scaned for entity classes
     *
     * @return
     */
    String basePackage() default "";

    /**
     * the parent class of target classes
     *
     * @return
     */
    Class<?> subTypesOf() default AvroEntity.class;

    /**
     * the required annotation of target classes
     *
     * @return
     */
    Class<? extends Annotation> annotatedWith() default Entity.class;
}
