package com.programm.ioutils.log.api;

import com.programm.ioutils.io.api.IOutput;

public class OutputLogger extends LevelLogger {

    private IOutput out;

    public OutputLogger() {
        this(null);
    }

    public OutputLogger(IOutput out) {
        this(LEVEL_TRACE, out);
    }

    public OutputLogger(int level) {
        this(level, null);
    }

    public OutputLogger(int level, IOutput out) {
        super(level);
        this.out = out;
    }

    @Override
    protected void log(String s, int methodLevel, Object... args) {
        if(out == null) return;

        if(level() <= methodLevel){
            out.println(s, args);
        }
    }

    public void setOut(IOutput out) {
        this.out = out;
    }
}
