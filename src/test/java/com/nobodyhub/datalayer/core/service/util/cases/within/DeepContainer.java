package com.nobodyhub.datalayer.core.service.util.cases.within;

import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import com.nobodyhub.datalayer.core.service.util.cases.ComplexClass;
import com.nobodyhub.datalayer.core.service.util.cases.SimpleEnum;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Fields are nested container of both primitive and complex class
 *
 * @author yan_h
 * @since 2018-04-20.
 */
public class DeepContainer implements AvroEntity {
    @Column
    private List<Map<String, SimpleEnum>> aSimpleEnumMapList;
    @Column
    private Map<String, List<Set<BigDecimal>>> aBigDecimalSetListMap;
    @Column(nullable = false)
    private Map<String, Map<String, Map<String, Set<List<ComplexClass>>>>> aComplexClassListSetMapMapMap;
    @Column
    private Set<Map<String, Map<String, List<Map<String, InheritedClass>>>>> aInheritedClassMapListMapMapSet;
}
