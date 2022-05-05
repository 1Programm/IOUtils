package com.programm.ioutils.log.api;

public class NullLogger implements ILogger {

    @Override
    public void trace(String s, Object... args) {}

    @Override
    public void debug(String s, Object... args) {}

    @Override
    public void info(String s, Object... args) {}

    @Override
    public void warn(String s, Object... args) {}

    @Override
    public void error(String s, Object... args) {}

    @Override
    public void logException(String msg, Throwable t) {}

    @Override
    public int level() {
        return LEVEL_NONE;
    }
}
