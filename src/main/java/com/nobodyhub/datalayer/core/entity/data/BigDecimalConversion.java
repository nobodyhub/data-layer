package com.nobodyhub.datalayer.core.entity.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Conversions;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

/**
 * @author Ryan
 */
@RequiredArgsConstructor
@Getter
public class BigDecimalConversion extends Conversions.DecimalConversion {

    private final int precision;
    private final int scale;

    /**
     * precison and scale are set according to {@link org.hibernate.mapping.Column}
     *
     * @return
     */
    @Override
    public Schema getRecommendedSchema() {
        return LogicalTypes.decimal(precision, scale).addToSchema(Schema.create(Schema.Type.BYTES));
    }
}
