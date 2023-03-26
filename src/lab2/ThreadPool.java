package lab2;

import utils.Time;

import java.util.LinkedList;

public class ThreadPool {
    private final   WorkerThread[] threads;
    private final   LinkedList<Task>[] queues;
    private boolean isStopped;
    private int     numOfRejectedTasks;


    public ThreadPool(int threadsNum) {
        isStopped = false;
        this.threads = new WorkerThread[threadsNum];
        this.queues  = new LinkedList[threadsNum];

        for (int i = 0; i < threadsNum; i++) {
            queues[i]  = new LinkedList<>();
            threads[i] = new WorkerThread(queues[i]);
            threads[i].start();
        }
    }

    public void addTask(Task task) {
        if (isStopped) throw new IllegalStateException("ThreadPool is stopped");

        for (LinkedList<Task> queue : queues) {
            if (queue.isEmpty()) {
                synchronized (queue) {
                    queue.offer(task);
                    queue.notify();
                    return;
                }
            }
        }

        numOfRejectedTasks++;
    }

    public void stopPool(boolean finishTasks) throws InterruptedException {
        isStopped = true;

        for (LinkedList<Task> queue : queues) {
                synchronized (queue) {
                    queue.notify();
                }
        }

        if (finishTasks) {
            for (WorkerThread thread : threads) thread.join();
        } else {
            for (WorkerThread thread : threads) thread.interrupt();
        }
    }

    public synchronized void pause() {
        isStopped = true;
    }

    public synchronized void resume() {
        isStopped = false;
    }

    public int getNumOfThreads() {
        return threads.length;
    }

    public int getNumOfRejectedTasks() {
        return numOfRejectedTasks;
    }

    public double getAverageWaitingTime() {
        long waitingTime = 0;
        long waitingCount = 0;

        for (WorkerThread thread : threads) {
            waitingTime += thread.getWaitingTime();
            waitingCount += thread.getWaitingCount();
        }

        if (waitingCount == 0) return 0.0;
        return ((double) waitingTime) / waitingCount;
    }

    public long getMinTimeQueue() {
        long minStartTime = Long.MAX_VALUE;

        for (WorkerThread thread : threads)
            if (thread.getMinQueueTime() < minStartTime)
                minStartTime = thread.minQueueTime;

        return minStartTime;
    }

    public long getMaxTimeQueue() {
        long maxEndTime = Long.MIN_VALUE;

        for (WorkerThread thread : threads)
            if (thread.getMaxQueueTime() > maxEndTime)
                maxEndTime = thread.getMaxQueueTime();

        return maxEndTime;
    }



    private class WorkerThread extends Thread {

        final LinkedList<Task> queue;

        private final Time time    = new Time();
        private long  minQueueTime = Long.MAX_VALUE;
        private long  maxQueueTime = Long.MIN_VALUE;
        private long  waitingTime;
        private long  waitingCount;

        public WorkerThread(LinkedList<Task> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {

            while (!isStopped) {
                time.start();
                Runnable task;

                synchronized (queue) {
                    while (queue.isEmpty() && !isStopped) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            interrupt();
                            return;
                        }
                    }

                    if(queue.isEmpty()) break;

                    updateWaitingTime();

                    time.start();
                    task = queue.peekFirst();
                }

                if(task != null) task.run();

                synchronized (queue) {
                    queue.pop();
                    updateMinMaxTime();
                }
            }
        }

        private void updateWaitingTime() {
            waitingTime += time.stop();
            waitingCount++;
        }

        private void updateMinMaxTime() {
            long end = time.stop();
            if (minQueueTime > end) minQueueTime = end;
            if (maxQueueTime < end) maxQueueTime = end;
        }

        public long getMaxQueueTime() {
            return maxQueueTime;
        }

        public long getMinQueueTime() {
            return minQueueTime;
        }

        public long getWaitingCount() {
            return waitingCount;
        }

        public long getWaitingTime() {
            return waitingTime;
        }
    }
}
