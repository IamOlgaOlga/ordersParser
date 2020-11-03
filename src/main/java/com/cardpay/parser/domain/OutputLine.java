package com.cardpay.parser.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = OutputLine.Builder.class)
public class OutputLine {
    private static final String SUCCESS = "OK";
    private final Long id;
    private final BigDecimal amount;
    private final String currency;
    private final String comment;
    private final String filename;
    private final Long line;
    private final String result;

    public static Builder success() {
        return new Builder(SUCCESS);
    }

    public static Builder fail(String reason) {
        return new Builder(reason);
    }

    @JsonPOJOBuilder
    public static final class Builder {
        private Long id;
        private BigDecimal amount;
        private String currency;
        private String comment;
        private String filename;
        private Long line;
        private String result;

        private Builder(String result) {
            this.result = result;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder setLine(Long line) {
            this.line = line;
            return this;
        }

        public Builder setResult(String result) {
            this.result = result;
            return this;
        }

        public OutputLine build() {
            return new OutputLine(id, amount, currency, comment, filename, line, result);
        }
    }
}
