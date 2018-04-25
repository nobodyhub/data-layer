package com.nobodyhub.datalayer.core.service.util.cases.within;

import com.nobodyhub.datalayer.core.service.util.cases.PrimitiveClass;
import com.nobodyhub.datalayer.core.service.util.cases.PrimitiveContainerClass;
import com.nobodyhub.datalayer.core.service.util.cases.SimpleEnum;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Complex class whose fields are not only primitive types
 * but without annotation {@link Entity}
 *
 * @author yan_h
 * @since 2018-04-20.
 */
public class ComplexClassWithoutAnnotation extends PrimitiveClass {
    @Column
    private PrimitiveClass primitiveClass;
    @Column(nullable = false)
    private PrimitiveContainerClass primitiveContainerClass;
    @Column
    private SimpleEnum simpleEnum;
    @Column(nullable = false)
    private ComplexEnum complexEnum;
}
