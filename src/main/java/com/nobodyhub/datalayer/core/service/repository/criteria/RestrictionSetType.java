package com.nobodyhub.datalayer.core.service.repository.criteria;

/**
 * @author yan_h
 * @since 2018-04-23.
 */
public enum RestrictionSetType {
    /**
     * Use <a href="https://en.wikipedia.org/wiki/Disjunctive_normal_form">DNF</a>v to organize the {@link RestrictionSet#restrictions}
     */
    DISJUNCTION,
    /**
     * Use <a href="https://en.wikipedia.org/wiki/Conjunctive_normal_form">CNF</a> to organize the {@link RestrictionSet#restrictions}
     */
    CONJUNCTION
}