package lab2;

import org.junit.jupiter.api.Test;
import utils.Data;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolTest {

    @Test
    public void generalTest() throws InterruptedException {
        int numOfThreads = 8;
        int numOfTasks = 20;

        ThreadPool pool = new ThreadPool(numOfThreads);

        for (int i = 0; i < numOfTasks; i++) {
            Task task = new Task(Data.randMillis());
            pool.addTask(task);
        }

        pool.stopPool(true);
        assertEquals(pool.getNumOfRejectedTasks(), numOfTasks - numOfThreads);
    }

    @Test
    public void resumeTest() throws InterruptedException {
        int numOfThreads = 8;
        int numOfTasks = 15;

        ThreadPool pool = new ThreadPool(numOfThreads);

        for (int i = 0; i < numOfTasks; i++) {
            pool.addTask(new Task(Data.randMillis()));
        }

        Thread.sleep(14000);

        pool.pause();
        Thread.sleep(1000);
        pool.resume();

        for (int i = 0; i < numOfTasks; i++) {
            pool.addTask(new Task(Data.randMillis()));
        }

        pool.stopPool(true);
        assertEquals(pool.getNumOfRejectedTasks(), numOfTasks * 2 - numOfThreads * 2);
    }
}