package com.programm.ioutils.io.console.formatters;

import com.programm.ioutils.io.api.IFormatter;

public class SurroundFormatter implements IFormatter {

    private final String prepend;
    private final String append;

    public SurroundFormatter(String prepend, String append) {
        this.prepend = prepend;
        this.append = append;
    }

    @Override
    public String format(String message, Object... args) {
        return prepend + message + append;
    }
}
