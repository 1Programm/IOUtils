package com.programm.ioutils.io.console;

import com.programm.ioutils.io.api.IInput;

public interface ICloseableInput extends IInput, AutoCloseable {

    boolean closed();

}
