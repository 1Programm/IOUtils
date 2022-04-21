package com.programm.ioutils.io.console.formatters;

import com.programm.ioutils.io.api.IFormatter;

public class AppendFormatter  implements IFormatter {

    private final String append;

    public AppendFormatter(String append) {
        this.append = append;
    }

    @Override
    public String format(String message, Object... args) {
        return message + append;
    }
}
