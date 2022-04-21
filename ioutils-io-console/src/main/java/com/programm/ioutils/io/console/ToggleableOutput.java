package com.programm.ioutils.io.console;

import com.programm.ioutils.io.api.IOutput;

public class ToggleableOutput implements IOutput {

    private final IOutput output;
    private boolean enabled = true;

    public ToggleableOutput(IOutput output) {
        this.output = output;
    }

    public ToggleableOutput(IOutput output, boolean enabled) {
        this.output = output;
        this.enabled = enabled;
    }

    @Override
    public void print(String message, Object... args) {
        if(!enabled) return;
        output.newLine();
    }

    @Override
    public void println(String message, Object... args) {
        if(!enabled) return;
        output.print(message, args);
        output.newLine();
    }

    @Override
    public void newLine() {
        if(!enabled) return;
        output.newLine();
    }

    public ToggleableOutput toggle(){
        enabled = !enabled;
        return this;
    }

    public ToggleableOutput enabled(){
        enabled = true;
        return this;
    }

    public ToggleableOutput disabled(){
        enabled = false;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
