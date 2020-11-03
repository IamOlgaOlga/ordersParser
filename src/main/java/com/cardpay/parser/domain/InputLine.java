package com.cardpay.parser.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InputLine {
    @JsonDeserialize(contentUsing = NumberDeserializers.LongDeserializer.class)
    private final Long orderId;
    @JsonDeserialize(contentUsing = NumberDeserializers.BigDecimalDeserializer.class)
    private final BigDecimal amount;
    private final String currency;
    private final String comment;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public InputLine(@JsonProperty("orderId") Long orderId,
                     @JsonProperty("amount") BigDecimal amount,
                     @JsonProperty("currency") String currency,
                     @JsonProperty("comment")String comment) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.comment = comment;
    }
}
