package com.nobodyhub.datalayer.core.cases;

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
public class DeepContainer {
    private List<Map<String, SimpleEnum>> aSimpleEnumMapList;
    private Map<String, List<Set<BigDecimal>>> aBigDecimalSetListMap;
    private Map<String, Map<String, Map<String, Set<List<ComplexClass>>>>> aComplexClassListSetMapMapMap;
    private Set<Map<String, Map<String, List<Map<String, InheritedClass>>>>> aInheritedClassMapListMapMapSet;
}
