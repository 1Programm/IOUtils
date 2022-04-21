package com.programm.ioutils.log.jlogger;

import com.programm.ioutils.io.api.IOutput;

class DefaultConsoleOut implements IOutput {

    @Override
    public void print(String message, Object... args) {
        System.out.print(message);
    }

}
