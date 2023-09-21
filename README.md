# Parallel computing course

<br />

## 🔷 Lab 1 [Study of basic operations with threads of execution]

#### Goal
Consider basic operations with threads of execution, learn to use non-blocking parallelization to solve the simplest mathematical problems

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

<img src="https://drive.google.com/uc?export=view&id=10cvWM3yvUB3ueVF14cXaEZY6NEIWwgRe" width="600">

<br />

#### Сonclusion

<br />

Based on the obtained results, it can be said that it is advisable to use parallelization on large volumes of data. On small text files, parallelization is inefficient, in some cases showing even worse results than the sequential algorithm, since the generation of threads slows down the solution somewhat. On data of medium length, using parallelization can speed up the solution by a factor of about 2, but the execution time increases with the number of threads.

<br />

Regarding the number of threads, their most optimal number is equal to the number of logical cores. When using the number of threads 2, 4, 8 times greater than the number of logical cores, we get approximately the same results as with an unchanged number. Let us explain this by the fact that although the number of threads is practically unlimited, the number of simultaneously active execution threads is limited by the number of physical cores, since one core really processes only 1 thread at a time (if the physical core is 2 logical cores - 2 threads at the same time).
