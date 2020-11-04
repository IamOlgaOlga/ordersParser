package com.cardpay.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * POJO for source file line's metadata.
 */
@Getter
@AllArgsConstructor
public class InputLineMetadata {
    /**
     * Source file name.
     */
    private final String fileName;
    /**
     * source file line number.
     */
    private final Long lineNumber;
    /**
     * source file line's content.
     */
    private final String lineContent;
}
