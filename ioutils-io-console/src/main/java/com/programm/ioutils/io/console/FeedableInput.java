package com.programm.ioutils.io.console;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class FeedableInput implements ICloseableInput {

    private final BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
    private final long timeout;

    private boolean closed;
    private Thread searchingThread;

    public FeedableInput() {
        this(-1);
    }

    public FeedableInput(long timeout) {
        this.timeout = Math.max(-1, timeout);
    }

    public void feed(String... inputs){
        for(String input : inputs) blockingQueue.add(input);
    }

    @Override
    public String next() {
        if(closed) throw new IllegalStateException("This input is already closed!");

        String nextInput = null;
        searchingThread = Thread.currentThread();
        try {
            if (timeout == -1) {
                nextInput = blockingQueue.take();
            }
            else {
                nextInput = blockingQueue.poll(timeout, TimeUnit.MILLISECONDS);
            }
        }
        catch (InterruptedException ignore){}
        finally {
            searchingThread = null;
        }

        return nextInput;
    }

    @Override
    public void close() throws Exception {
        if(searchingThread != null) searchingThread.interrupt();
        closed = true;
    }

    @Override
    public boolean closed() {
        return closed;
    }
}
