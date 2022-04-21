package com.programm.ioutils.io.api;

public interface IFormatter {

    /**
     * Formats the message and args and returns the formatted String.
     * @param message the message to format
     * @param args the args to format
     * @return the formatted result
     */
    String format(String message, Object... args);

}
