package lab1;

import utils.File;
import utils.Time;

import java.util.ArrayList;
import java.util.List;

public class TimeComparison {

    public static List<Long> timeComparison(String filePath, int numOfThreads)
            throws InterruptedException {

        Time time = new Time();
        char[] str = File.getCharArr(filePath);

        List<Long> result = new ArrayList<>();
        result.add((long) File.getFileSizeMB(filePath));

        time.start();
        StringReverse.sequentialSolution(str);
        result.add(time.stop());

        for(int i = 1; i < numOfThreads; i++) {
            time.start();
        //    StringReverse.parallelSolution(str, (int) Math.pow(2, i));
            result.add(time.stop());
        }

        return result;
    }

    public static void printResults() throws InterruptedException {
        List<List<Long>> result = new ArrayList<>();
        int num = 8;

        for (int i = 0; i < 9; i++) {
            result.add(timeComparison(String.valueOf(i), num));
        }

        System.out.println("\nsize(mb) / \t\t");
        System.out.print("num of threads\t\t");

        for(int i = 0; i < num; i++) {
            System.out.print((int) Math.pow(2, i) + "\t");
            if(i == num - 1) System.out.println();
        }

        for (List<Long> cur : result) {
            System.out.print("\t\t\t" + cur.get(0) + "\t\t");

            for (int j = 1; j < num + 1; j++) {
                System.out.print(cur.get(j) + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        printResults();
    }
}
