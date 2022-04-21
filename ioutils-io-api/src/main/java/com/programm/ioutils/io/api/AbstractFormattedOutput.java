package com.programm.ioutils.io.api;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFormattedOutput implements IFormattedOutput {

    private final List<IFormatter> formatters = new ArrayList<>();

    protected String getFormattedOutput(String message, Object... args) {
        for(IFormatter formatter : formatters){
            message = formatter.format(message, args);
        }

        return message;
    }

    @Override
    public AbstractFormattedOutput addFormatter(IFormatter formatter) {
        this.formatters.add(formatter);
        return this;
    }

    @Override
    public AbstractFormattedOutput removeFormatter(IFormatter formatter) {
        this.formatters.remove(formatter);
        return this;
    }

}
