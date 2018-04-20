package com.nobodyhub.datalayer.core.cases;

import javax.persistence.Column;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Container which use Complex record as element
 *
 * @author yan_h
 * @since 2018-04-20.
 */
public class ComplexContainer {
    @Column
    private List<ComplexClass> complexClassList;
    @Column
    private Map<String, ComplexClass> complexClassMap;
    @Column
    private Set<ComplexClass> complexClassSet;
}
