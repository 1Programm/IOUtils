package com.programm.ioutils.io.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ScannerInput implements ICloseableInput {

    private final Scanner scanner;
    private boolean closed;

    public ScannerInput(InputStream is) {
        this(new Scanner(is));
    }

    public ScannerInput(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String next() {
        if(closed) throw new IllegalStateException("This input is already closed!");
        return scanner.nextLine();
    }

    @Override
    public boolean closed() {
        return closed;
    }

    @Override
    public void close() throws Exception {
        scanner.close();
        closed = true;

        IOException ex = scanner.ioException();
        if(ex != null) throw ex;
    }
}
