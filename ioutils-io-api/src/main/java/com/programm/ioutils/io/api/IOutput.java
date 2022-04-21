package com.programm.ioutils.io.api;

public interface IOutput {

    void print(String message, Object... args);

    default void newLine() {
        print("\n");
    }

    default void println(String message, Object... args){
        print(message, args);
        newLine();
    }

}
