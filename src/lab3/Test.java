package lab3;

import org.testng.internal.collections.Pair;
import utils.Time;

import java.util.Random;

public class Test {

    public static void fillArr (int[] arr) {
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) arr[i] = random.nextInt();
    }

    public static void timeTest() {

        for (int i = 0; i < 4; i++) {
            int size = (int) Math.pow(100, i + 1);
            int[] arr = new int[size];
            fillArr(arr);

            System.out.println("\n------array size == " + size);
            Time time = new Time();

            time.start();
            Pair<Integer, Integer> res = Solution.getSequential(arr);
            System.out.println("sequential res:\t\t" + res + "  " + time.stop() + "mcs");

            time.start();
            res = Solution.getBlocking(arr);
            System.out.println("blocking res:\t\t" + res + "  " + time.stop() + "mcs");

            time.start();
            res = Solution.getNonBlocking(arr);
            System.out.println("non-block res:\t\t" + res + "  " + time.stop() + "mcs");
        }

    }

    public static void main(String[]args)  {
        timeTest();
    }

}
