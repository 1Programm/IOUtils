package com.programm.ioutils.log.api;

import com.programm.ioutils.io.api.IOutput;

public interface IConfigurableLogger extends ILogger {

    void setNextLogInfo(Class<?> cls, String methodName);

    IConfigurableLogger level(int level) throws LoggerConfigException;

    IConfigurableLogger format(String format) throws LoggerConfigException;

    IConfigurableLogger packageLevel(String pkg, int level) throws LoggerConfigException;

    IConfigurableLogger output(IOutput output) throws LoggerConfigException;

}
