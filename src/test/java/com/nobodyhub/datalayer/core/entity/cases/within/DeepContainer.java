package com.nobodyhub.datalayer.core.entity.cases.within;

import com.nobodyhub.datalayer.core.entity.cases.ComplexClass;
import com.nobodyhub.datalayer.core.entity.cases.SimpleEnum;
import com.nobodyhub.datalayer.core.entity.common.AvroEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Entity
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
