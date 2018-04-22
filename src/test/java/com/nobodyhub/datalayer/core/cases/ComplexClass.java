package com.nobodyhub.datalayer.core.cases;

import com.nobodyhub.datalayer.core.cases.within.ComplexEnum;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Complex class whose fields are not only primitive types
 *
 * @author yan_h
 * @since 2018-04-20.
 */
@Entity
public class ComplexClass {
    @Column
    private PrimitiveClass primitiveClass;
    @Column(nullable = false)
    private PrimitiveContainerClass primitiveContainerClass;
    @Column
    private SimpleEnum simpleEnum;
    @Column(nullable = false)
    private ComplexEnum complexEnum;
}
