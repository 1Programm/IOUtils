package com.programm.ioutils.log.api;

public class LoggerConfigException extends RuntimeException {

    public LoggerConfigException() {
    }

    public LoggerConfigException(String message) {
        super(message);
    }

    public LoggerConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggerConfigException(Throwable cause) {
        super(cause);
    }
}
