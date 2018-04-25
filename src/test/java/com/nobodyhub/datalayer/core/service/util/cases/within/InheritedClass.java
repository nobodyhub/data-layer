package com.nobodyhub.datalayer.core.service.util.cases.within;

import com.nobodyhub.datalayer.core.service.util.cases.PrimitiveClass;
import com.nobodyhub.datalayer.core.service.util.cases.SimpleEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;

/**
 * Class inherites fields from parent class
 *
 * @author yan_h
 * @since 2018-04-20.
 */
@Entity
public class InheritedClass extends PrimitiveClass {
    @Column
    private String anotherString;
    @Column
    private SimpleEnum simpleEnum;
    @Column(nullable = false)
    private List<PrimitiveClass> primitiveClassList;
}
