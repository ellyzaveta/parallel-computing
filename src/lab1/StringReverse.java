package lab1;

class MyThread extends Thread {
    int position;           //індекс у вхідному рядку, з якого поточний потік повинен почати виконання
    int length;             //довжина частини рядка, яку має розвернути поточний потік
    char[] finalStr;        //рядок результат
    char[] str;             //вхідний рядок

    public MyThread(int position, int length, char[] finalStr, char[] str) {
        this.position = position;
        this.length = length;
        this.finalStr = finalStr;
        this.str = str;
    }

    //реверс частини рядка поточним потоком
    @Override
    public void run() {
        for(int i = position; i < position + length; i++) {
            finalStr[str.length - i - 1] = str[i];
        }
    }
}

public class StringReverse {

    public static char[] sequentialSolution(char[] str) {
        char[] finalStr = new char[str.length];

        for(int i = 0; i < str.length; i++) {
            finalStr[str.length - i - 1] = str[i];
        }

        return finalStr;
    }

    public static char[] parallelSolution(char[] str, int numOfThreads) throws InterruptedException {
        int length = str.length;
        char[] finalStr = new char[length];

        MyThread[] threads = new MyThread[numOfThreads];

        int lengthPerThread = length / numOfThreads; //довжина рядка на кожен процес
        int remainingLength = length % numOfThreads; //довжина залишку, якщо вхідний рядок не кратний кількості потоків

        for (int i = 0; i < numOfThreads; i++) {
            if(remainingLength != 0 && i == numOfThreads - 1)
                threads[i] = new MyThread(i * lengthPerThread, lengthPerThread + remainingLength, finalStr, str);
            else
                threads[i] = new MyThread(i * lengthPerThread, lengthPerThread, finalStr, str);
            threads[i].start();
        }

        for (int i = 0; i < numOfThreads; i++) {
            threads[i].join();
        }

        return finalStr;
    }
}
