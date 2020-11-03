package com.cardpay.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * POJO for source file line's metadata.
 */
@Getter
@AllArgsConstructor
@ToString
public class InputLineMetadata {
    /**
     * Source file name.
     */
    private final String fileName;
    /**
     * source file line number.
     */
    private final long lineNumber;
    /**
     * source file line's content.
     */
    private final String lineContent;
}
