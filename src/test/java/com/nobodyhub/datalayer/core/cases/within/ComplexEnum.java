package com.nobodyhub.datalayer.core.cases.within;

import lombok.Getter;

/**
 * A complex enum with fielsd as well as properties
 *
 * @author yan_h
 * @since 2018-04-20.
 */
public enum ComplexEnum {
    C1(1, "1"), C2(2, "2"), C3(3, "3");

    @Getter
    int intRaw;
    @Getter
    String stringRaw;

    ComplexEnum(int intRaw, String stringRaw) {
        this.intRaw = intRaw;
        this.stringRaw = stringRaw;
    }

    public static ComplexEnum valueOf(int intRaw, String stringRaw) {
        for (ComplexEnum value : ComplexEnum.values()) {
            if (value.getIntRaw() == intRaw
                    && value.getStringRaw().equals(stringRaw)) {
                return value;
            }
        }
        return C1;
    }
}
