package com.programm.ioutils.log.api;

public interface ILogger {

    String NAME_TRACE = "TRACE";
    String NAME_DEBUG = "DEBUG";
    String NAME_INFO = "INFO";
    String NAME_WARN = "WARN";
    String NAME_ERROR = "ERROR";
    String NAME_NONE = "NONE";

    int LEVEL_TRACE = 0;
    int LEVEL_DEBUG = 1;
    int LEVEL_INFO  = 2;
    int LEVEL_WARN  = 3;
    int LEVEL_ERROR = 4;
    int LEVEL_NONE  = 5;

    static String levelToString(int level){
        switch (level){
            case LEVEL_TRACE:
                return NAME_TRACE;
            case LEVEL_DEBUG:
                return NAME_DEBUG;
            case LEVEL_INFO:
                return NAME_INFO;
            case LEVEL_WARN:
                return NAME_WARN;
            case LEVEL_ERROR:
                return NAME_ERROR;
            case LEVEL_NONE:
                return NAME_NONE;
            default:
                throw new IllegalArgumentException("Level [" + level + "] is not defined!");
        }
    }

    static int fromString(String level){
        switch (level.toUpperCase()){
            case NAME_TRACE:
                return LEVEL_TRACE;
            case NAME_DEBUG:
                return LEVEL_DEBUG;
            case NAME_INFO:
                return LEVEL_INFO;
            case NAME_WARN:
                return LEVEL_WARN;
            case NAME_ERROR:
                return LEVEL_ERROR;
            case NAME_NONE:
                return LEVEL_NONE;
            default:
                throw new IllegalArgumentException("Level [" + level + "] is not defined!");
        }
    }

    void trace(String s, Object... args);
    void debug(String s, Object... args);
    void info(String s, Object... args);
    void warn(String s, Object... args);
    void error(String s, Object... args);

    int level();

    default boolean isTrace(){
        return level() <= LEVEL_TRACE;
    }

    default boolean isDebug(){
        return level() <= LEVEL_DEBUG;
    }

    default boolean isInfo(){
        return level() <= LEVEL_INFO;
    }

    default boolean isWarn(){
        return level() <= LEVEL_WARN;
    }

    default boolean isError(){
        return level() <= LEVEL_ERROR;
    }

    default boolean isNone(){
        return level() >= LEVEL_NONE;
    }
}
