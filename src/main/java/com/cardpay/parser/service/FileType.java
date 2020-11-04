package com.cardpay.parser.service;

import java.util.Arrays;
import java.util.Optional;

/**
 * Input file type
 */
public enum FileType {
    /**
     * .csv file extension type
     */
    CSV,
    /**
     * .json file extension type
     */
    JSON,
    ;

    /**
     * Maps file extension to file type
     *
     * @param extension file extension
     * @return {@link FileType}
     */
    public static Optional<FileType> resolve(String extension) {
        return Arrays.stream(values())
                .filter(type -> extension.toUpperCase().equals(type.toString()))
                .findFirst();
    }
}
