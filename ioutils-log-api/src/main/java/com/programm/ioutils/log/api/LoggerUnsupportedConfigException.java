package com.programm.ioutils.log.api;

public class LoggerUnsupportedConfigException extends RuntimeException {

    public LoggerUnsupportedConfigException() {
    }

    public LoggerUnsupportedConfigException(String message) {
        super(message);
    }

    public LoggerUnsupportedConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggerUnsupportedConfigException(Throwable cause) {
        super(cause);
    }
}
