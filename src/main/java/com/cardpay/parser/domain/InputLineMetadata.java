package com.cardpay.parser.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InputLineMetadata {
    private final String fileName;
    private final long lineNumber;
    private final String lineContent;
}
