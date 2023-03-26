package lab2;

import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

    private final long millis;

    public Task(long millis) {
        this.millis = millis;
    }

    public void run() {
        try {
            System.out.println("Task is running " + TimeUnit.MILLISECONDS.toSeconds(millis) + "s");
            Thread.sleep(millis);
        } catch (InterruptedException ignored) { }
    }
}
