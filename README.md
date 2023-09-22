# Parallel computing course

<br />

## Сontent
* [Study of basic operations with threads](#study-of-basic-operations-with-threads)
* [Study of basic synchronization primitives](#study-of-basic-synchronization-primitives)
* [Study of atomic variables and atomic operations](#study-of-atomic-variables-and-atomic-operations)
* [Implementation of client-server application for basic mathematical operations on an array](#implementation-of-client-server-application-for-basic-mathematical-operations-on-an-array)
<br />

## Study of basic operations with threads

#### Goal
Consider basic operations with threads of execution, learn to use non-blocking parallelization to solve the simplest mathematical problems.

#### Task
Create a text string and reverse it (for example, “abc” will become “cba”).
Solve the problem without using parallelization and using parallelization and plot the dependence of execution time on the number of processes and the length of text files.

#### Result

<br />

| File size (МБ) / Num of threads | 1   | 2   | 4   | 8   | 16  | 32  | 64  | 128 |
|-----------------------------|---------------------|-----|-----|-----|-----|-----|-----|-----|
| <1                          | 2                   | 5   | 3   | 1   | 2   | 2   | 3   | 6   |
| 5                           | 3                   | 2   | 1   | 1   | 1   | 2   | 2   | 4   |
| 13                          | 3                   | 3   | 1   | 2   | 2   | 3   | 2   | 5   |
| 20                          | 8                   | 4   | 3   | 2   | 3   | 3   | 5   | 4   |
| 41                          | 9                   | 8   | 5   | 6   | 5   | 7   | 6   | 13  |
| 74                          | 17                  | 11  | 10  | 9   | 9   | 9   | 9   | 10  |
| 141                         | 30                  | 20  | 17  | 16  | 17  | 18  | 17  | 17  |
| 197                         | 129                 | 29  | 21  | 22  | 24  | 24  | 22  | 24  |
| 265                         | 126                 | 37  | 29  | 29  | 29  | 30  | 30  | 30  |

<br />

<img src="https://drive.google.com/uc?export=view&id=10cvWM3yvUB3ueVF14cXaEZY6NEIWwgRe" width="630">

<br />

#### Сonclusion

<br />

Based on the obtained results, it can be said that it is advisable to use parallelization on large volumes of data. On small text files, parallelization is inefficient, in some cases showing even worse results than the sequential algorithm, since the generation of threads slows down the solution somewhat. On data of medium length, using parallelization can speed up the solution by a factor of about 2, but the execution time increases with the number of threads.

<br />

Regarding the number of threads, their most optimal number is equal to the number of logical cores. When using the number of threads 2, 4, 8 times greater than the number of logical cores, we get approximately the same results as with an unchanged number. Let us explain this by the fact that although the number of threads is practically unlimited, the number of simultaneously active execution threads is limited by the number of physical cores, since one core really processes only 1 thread at a time (if the physical core is 2 logical cores - 2 threads at the same time).

<br />

## Study of basic synchronization primitives

#### Goal
Consider the basic synchronization primitives and their features. Consider approaches to building software using parallelism and familiarize yourself with the classic problem of parallelism in the form of a thread pool.

#### Task

- Individual task: 
The thread pool is served by 8 worker threads. There is no queue of tasks as such. Tasks are immediately added to the free workflow. If all work streams are busy, the task is rejected. The task takes a random time between 10 and 14 seconds. (Technically, there is no thread queue, but in this implementation it is possible to implement 8 separate queues for one task, which, in fact, is the absence of a queue, because a queue is one active task).

- General task:
Implement your own thread pool with the characteristics specified in the individual task. The thread pool must have the ability to complete its work correctly (immediately, with the abandonment of all active tasks, and with the completion of active tasks), the ability to temporarily stop its work. The operations of initializing and destroying the pool, adding and removing tasks from the queue must be thread-safe.
Check and prove the correct operation of the program using the console input/output system. Perform time-limited testing and calculate the number of threads created and the average time a thread is in the waiting state. Determine the maximum and minimum time until the queue was filled, the number of rejected tasks (for limited queues).

#### Result

The thread pool implementation is concentrated in the ThreadPool class. The main idea of ensuring thread safety and optimal access to task queues is to use the synchronized keyword, which allows you to block access to a part of the code if it is already being used by another thread.

According to the individual task, the thread pool contains a given number of worker threads and a corresponding number of queues. If one of the queues is empty, the received task is added to it for execution, otherwise it is rejected. Each thread, in turn, checks its queue for a task to execute. If there is a task, the thread executes it, otherwise it waits for it to arrive. After completing a task, the workflow removes the task from the queue, thereby signaling its readiness to accept a new task for execution.

<br />

| Metric                           | Value |
|----------------------------------|-------|
| Total time                       | 40    |
| Total num of threads             | 8     |
| Total num of tasks               | 150   |
| Average thread waiting time      | 0.17  |
| Num of rejected tasks            | 126   |
| Num of executed tasks            | 24    |
| Min time while queue was filled  | 10    |
| Max time while queue was filled  | 14    |

<br />

#### Conclusion

Based on the obtained test results, it can be said that only ~ 16% of the input tasks were completed. This omission of a large part of the tasks can lead to potentially incomplete or incorrect results. This can be a problem in applications where all tasks are equally important and must all be completed, or in situations where partial results are unacceptable.

<br />

However, on the other hand, implementing a thread pool with task deflection can be useful for specific use cases where the workload is well defined and the system has limited resources or strict performance requirements. So, for example, a thread pool on a web server can be designed to handle incoming requests from clients. If all worker threads are busy processing requests, new requests will be rejected until a worker thread becomes available. This can prevent server overload and crash.

<br />

## Study of atomic variables and atomic operations

#### Goal
Consider the concept of atomicity, learn how to work with atomic variables, and familiarize yourself with the approach of writing parallel code without blocking.

#### Individual task:
Find the difference of all odd elements of an array, the smallest odd number.

#### General task:
- Implement an individual task without using threads. Measure the time of the task.
- Implement an individual task using blocking synchronization primitives. Measure the task execution time, as well as the amount of time spent waiting for primitives to be unlocked.
- Implement an individual task using atomic variables and CAS operations. Measure the time of the task.
Repeat steps 2, 3 and 4 using different data dimensions. Present the results in the form of graphs.

#### Result

<br />

<img src="https://drive.google.com/uc?export=view&id=1b-W-PvWi1_IGbtoTicRWdcTZMkggrRFJ" width="630">

<br />

#### Conclusions

Based on the obtained results, we can say that on small and medium-sized data, operations with atomic variables are performed faster than operations with blocking, since they do not require blocking and unlocking of the monitor. However, on very large data when performing this specific task, atomic variables show worse results over time.

Atomic variables are typically used for small operations, such as incrementing or decrementing a numeric value, without having to block access to the resource for other threads. They ensure the speed and efficiency of such operations, as they do not require blocking resources for other threads.

Blocking primitives, such as mutexes or semaphores, are used for large operations that can take a lot of time and resources. They protect access to the resource for all other threads until the operation is complete. The use of blocking primitives provides security and prevents conflicts during the execution of critical sections of the code.

<br />

## Implementation of client-server application for basic mathematical operations on an array

#### Task
Develop a client-server application to solve the task from the l.r. No. 1, by transferring an array of data from the client to the server, and then receiving the result back on the client side.
Develop an application-level protocol for client-server interaction, for this, consider several steps in the interaction process:
1. Sending data and configuration of calculations (in accordance with the tasks of the option from LR No. 1).
2. Sending a command to start calculations.
3. Result request with two possible response options: current status (if the result is still being calculated) and receiving the final result.
4. Support several clients at the same time.

#### Results

All server/client pairs must have a specific protocol by which they communicate with each other, otherwise the data they exchange will not make sense.

<br />

<img src="https://drive.google.com/uc?export=view&id=1bL68a3ieKtjobmtWwy2HrFj3uei_hKSP" width="630">

#### Conclusions

To ensure client-server interaction, an application-level protocol was developed, which includes several steps. First, data and computation configuration are sent from the client to the server. Then a command is sent to start the calculation. There are two response options for getting the result: the current status, if the result is still being calculated, or getting the final result.

<br />

A separate functionality of the application is the support of several clients at the same time, which allows you to perform tasks more efficiently.

<br />

The client and server were implemented in different programming languages. This approach, for example, can be used to implement the server part using a language that has good support for multithreading or distributed computing, while the client part can use a lightweight language that is convenient for user interface development.

