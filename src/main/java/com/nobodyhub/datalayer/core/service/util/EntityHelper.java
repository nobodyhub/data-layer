package com.nobodyhub.datalayer.core.service.util;

import org.hibernate.metadata.ClassMetadata;

/**
 * @author yan_h
 * @since 2018-04-26.
 */
public final class EntityHelper {
    private EntityHelper(){}
    public static String PLACEHOLDER_ID = "@Id";

    public static Class getFieldClass(ClassMetadata meta, String fieldName) {
        if (PLACEHOLDER_ID.equals(fieldName)) {
            return meta.getIdentifierType().getReturnedClass();
        }
        return meta.getPropertyType(fieldName).getReturnedClass();
    }
}
