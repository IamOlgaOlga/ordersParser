package com.cardpay.parser.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * POJO for input line from JSON file.
 */
@Getter
public class InputLine {
    /**
     * Order ID
     */
    @JsonDeserialize(contentUsing = NumberDeserializers.LongDeserializer.class)
    private final Long orderId;
    /**
     * Order amount
     */
    @JsonDeserialize(contentUsing = NumberDeserializers.BigDecimalDeserializer.class)
    private final BigDecimal amount;
    /**
     * Currency
     */
    private final String currency;
    /**
     * Comment from order
     */
    private final String comment;

    /**
     * Constructor for {@link InputLine}
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public InputLine(@JsonProperty("orderId") Long orderId,
                     @JsonProperty("amount") BigDecimal amount,
                     @JsonProperty("currency") String currency,
                     @JsonProperty("comment") String comment) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
    }
}
