package com.programm.ioutils.log.api;

public interface IConfigurableLogger extends ILogger {

    void setNextLogInfo(Class<?> cls, String methodName);

    IConfigurableLogger config(String name, Object... args) throws LoggerConfigException, LoggerUnsupportedConfigException;

}
