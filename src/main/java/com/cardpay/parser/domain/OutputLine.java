package com.cardpay.parser.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * POJO for result output line.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = OutputLine.Builder.class)
public class OutputLine {
    /**
     * Constant for success status of parsing process.
     */
    private static final String SUCCESS = "OK";
    /**
     * Order ID
     */
    private final Long id;
    /**
     * Order amount
     */
    private final BigDecimal amount;
    /**
     * Currency of order
     */
    private final String currency;
    /**
     * Comment from order
     */
    private final String comment;
    /**
     * Source file name
     */
    private final String filename;
    /**
     * Source file line's number
     */
    private final Long line;
    /**
     * Parsing result
     */
    private final String result;

    /**
     * Provide a {@link Builder} in case SUCCESS parsing result.
     * @return {@link Builder} to build a {@link OutputLine} object
     */
    public static Builder success() {
        return new Builder(SUCCESS);
    }

    /**
     * Provide a {@link Builder} in case FAILED parsing result.
     * @return {@link Builder} to build a {@link OutputLine} object
     */
    public static Builder fail(String reason) {
        return new Builder(reason);
    }

    /**
     * Builder for {@link OutputLine} objects.
     */
    @JsonPOJOBuilder
    public static final class Builder {
        /**
         * @see OutputLine
         */
        private Long id;
        /**
         * @see OutputLine
         */
        private BigDecimal amount;
        /**
         * @see OutputLine
         */
        private String currency;
        /**
         * @see OutputLine
         */
        private String comment;
        /**
         * @see OutputLine
         */
        private String filename;
        /**
         * @see OutputLine
         */
        private Long line;
        /**
         * @see OutputLine
         */
        private String result;

        /**
         * Constructor
         * @param result parsing result
         */
        private Builder(String result) {
            this.result = result;
        }

        /**
         * Setter for order ID
         * @param id order ID
         * @return {@link Builder}
         */
        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        /**
         * Setter for order amount
         * @param amount order amount
         * @return {@link Builder}
         */
        public Builder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Setter for order amount currency
         * @param currency order amount currency
         * @return {@link Builder}
         */
        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        /**
         * Setter for comment from order
         * @param comment comment from order
         * @return {@link Builder}
         */
        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Setter for source file
         * @param filename source file
         * @return {@link Builder}
         */
        public Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        /**
         * Setter for source file line's number
         * @param line source file line's number
         * @return {@link Builder}
         */
        public Builder setLine(Long line) {
            this.line = line;
            return this;
        }

        /**
         * Setter for parsing result
         * @param result parsing result
         * @return {@link Builder}
         */
        public Builder setResult(String result) {
            this.result = result;
            return this;
        }

        /**
         * Build an {@link OutputLine} object.
         * @return {@link OutputLine} object.
         */
        public OutputLine build() {
            return new OutputLine(id, amount, currency, comment, filename, line, result);
        }
    }
}
