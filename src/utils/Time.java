package utils;

public class Time {
    private long start;

    public void start() {
        start = System.nanoTime();
    }

    public long stop() {
        return (System.nanoTime() - start) / 1000000;
    }
}
