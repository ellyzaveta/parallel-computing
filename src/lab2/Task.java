package lab2;

import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

    private final int num;
    private final long millis;

    public Task(int num, long millis) {
        this.num = num;
        this.millis = millis;
    }

    public void run() {
        try {
            System.out.println("Task " + num + " is running " + TimeUnit.MILLISECONDS.toSeconds(millis) + "s");
            Thread.sleep(millis);
        } catch (InterruptedException ignored) { }
    }
}
