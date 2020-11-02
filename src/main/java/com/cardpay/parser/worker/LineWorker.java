package com.cardpay.parser.worker;

import com.cardpay.parser.service.parser.Parser;

public class LineWorker implements Runnable{

    private final Parser parser;

    private final String fileName;

    private final int lineNumber;

    private final String line;

    public LineWorker(Parser parser, String fileName, int lineNumber, String line){
        this.parser = parser;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.line = line;
    }

    @Override
    public void run() {
    }
}
