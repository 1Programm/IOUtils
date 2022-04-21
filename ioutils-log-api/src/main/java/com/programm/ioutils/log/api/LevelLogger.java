package com.programm.ioutils.log.api;

public abstract class LevelLogger implements ILogger {

    private int level;

    public LevelLogger() {
        this(LEVEL_TRACE);
    }

    public LevelLogger(int level) {
        this.level = level;
    }

    protected abstract void log(String s, int methodLevel, Object... args);

    @Override
    public void trace(String s, Object... args) {
        log(s, LEVEL_TRACE, args);
    }

    @Override
    public void debug(String s, Object... args) {
        log(s, LEVEL_DEBUG, args);
    }

    @Override
    public void info(String s, Object... args) {
        log(s, LEVEL_INFO, args);
    }

    @Override
    public void warn(String s, Object... args) {
        log(s, LEVEL_WARN, args);
    }

    @Override
    public void error(String s, Object... args) {
        log(s, LEVEL_ERROR, args);
    }

    @Override
    public int level() {
        return level;
    }

    public LevelLogger level(int level){
        this.level = level;
        return this;
    }
}
