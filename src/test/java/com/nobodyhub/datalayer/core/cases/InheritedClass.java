package com.nobodyhub.datalayer.core.cases;

import javax.persistence.Column;
import java.util.List;

/**
 * Class inherites fields from parent class
 *
 * @author yan_h
 * @since 2018-04-20.
 */
public class InheritedClass extends PrimitiveClass {
    @Column
    private String anotherString;
    @Column
    private SimpleEnum simpleEnum;
    @Column(nullable = false)
    private List<PrimitiveClass> primitiveClassList;
}
