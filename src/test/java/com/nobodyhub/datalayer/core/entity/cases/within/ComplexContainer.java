package com.nobodyhub.datalayer.core.entity.cases.within;

import com.nobodyhub.datalayer.core.entity.cases.ComplexClass;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Container which use Complex record as element
 *
 * @author yan_h
 * @since 2018-04-20.
 */
@Entity
public class ComplexContainer {
    @Column
    private List<ComplexClass> complexClassList;
    @Column(nullable = false)
    private Map<String, ComplexClass> complexClassMap;
    @Column
    private Set<ComplexClass> complexClassSet;
}
