package utils;

import java.util.concurrent.TimeUnit;

public class Time {
    private long start;

    public void start() {
        start = System.nanoTime();
    }

    public long stop() {
        return TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
    }
}
