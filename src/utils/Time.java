package utils;

public class Time {
    private long start;

    public void start() {
        start = System.nanoTime() / 1000;
    }

    public long stop() {
        return (System.nanoTime() / 1000 - start);
    }
}
