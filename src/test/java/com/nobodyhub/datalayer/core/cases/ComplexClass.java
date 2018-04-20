package com.nobodyhub.datalayer.core.cases;

import javax.persistence.Column;

/**
 * Complex class whose fields are not only primitive types
 *
 * @author yan_h
 * @since 2018-04-20.
 */
public class ComplexClass {
    @Column
    private PrimitiveClass primitiveClass;
    @Column
    private PrimitiveContainerClass primitiveContainerClass;
    @Column
    private SimpleEnum simpleEnum;
    @Column
    private ComplexEnum complexEnum;
}
