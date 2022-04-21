package com.programm.ioutils.io.console;

import com.programm.ioutils.io.api.AbstractFormattedOutput;
import com.programm.ioutils.io.api.IFormatter;

public class ConsoleOutput extends AbstractFormattedOutput {

    @Override
    public void print(String message, Object... args) {
        String formattedMessage = getFormattedOutput(message, args);
        System.out.print(formattedMessage);
    }

    @Override
    public void newLine() {
        System.out.println();
    }

    @Override
    public ConsoleOutput addFormatter(IFormatter formatter) {
        super.addFormatter(formatter);
        return this;
    }

    @Override
    public ConsoleOutput removeFormatter(IFormatter formatter) {
        super.removeFormatter(formatter);
        return this;
    }
}
