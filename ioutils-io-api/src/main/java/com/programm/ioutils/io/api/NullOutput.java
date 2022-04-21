package com.programm.ioutils.io.api;

public class NullOutput implements IOutput {

    @Override
    public void print(String message, Object... args) {}

    @Override
    public void newLine() {}

    @Override
    public void println(String message, Object... args) {}
}
