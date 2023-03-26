package lab2;

import utils.Data;
import utils.Time;

import java.text.DecimalFormat;

public class Test {

    public static void test() throws InterruptedException {
        ThreadPool pool  = new ThreadPool(8);
        Time time        = new Time();
        DecimalFormat df = new DecimalFormat("#.00");

        long end = System.currentTimeMillis() + 30 * 1000;

        time.start();

        int totalNumOfTasks = 0;
        while (System.currentTimeMillis() < end) {
            pool.addTask(new Task(totalNumOfTasks, Data.randMillis()));
            totalNumOfTasks++;
            if (totalNumOfTasks % 5 == 0) Thread.sleep(1000);
        }

        pool.stopPool(true);
        long totalTime = time.stop();

        int numOfExecutedTasks = totalNumOfTasks - pool.getNumOfRejectedTasks();

        System.out.println();
        System.out.println("total time:             " + totalTime);
        System.out.println("total num of threads:   " + pool.getNumOfThreads());
        System.out.println("total num of tasks:     " + totalNumOfTasks);
        System.out.println();
        System.out.println("av thread waiting time: " + df.format(pool.getAverageWaitingTime()));
        System.out.println("num of rejected tasks : " + pool.getNumOfRejectedTasks());
        System.out.println("num of executed tasks : " + numOfExecutedTasks);
        System.out.println("min time while queue was filled: " + pool.getMinTimeQueue());
        System.out.println("max time while queue was filled: " + pool.getMaxTimeQueue());
    }

    public static void main(String[] args) throws InterruptedException {
        test();
    }
}