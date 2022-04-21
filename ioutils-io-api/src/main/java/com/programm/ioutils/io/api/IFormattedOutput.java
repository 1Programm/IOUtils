package com.programm.ioutils.io.api;

public interface IFormattedOutput extends IOutput{

    IFormattedOutput addFormatter(IFormatter formatter);
    IFormattedOutput removeFormatter(IFormatter formatter);

}
