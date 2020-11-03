package com.cardpay.parser.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class InputLineMetadata {
    private final String fileName;
    private final long lineNumber;
    private final String lineContent;
}
