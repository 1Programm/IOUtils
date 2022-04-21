package com.programm.ioutils.io.console.formatters;

import com.programm.ioutils.io.api.IFormatter;

public class PrependFormatter implements IFormatter {

    private final String prepend;

    public PrependFormatter(String prepend) {
        this.prepend = prepend;
    }

    @Override
    public String format(String message, Object... args) {
        return prepend + message;
    }
}
