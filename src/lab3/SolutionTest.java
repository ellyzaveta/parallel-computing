package lab3;

import org.junit.jupiter.api.Test;
import org.testng.internal.collections.Pair;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {

    public static void fillArr(int[] arr) {
        Random random = new Random();
        for(int i = 0; i < arr.length; i++) arr[i] = random.nextInt();
    }

    @Test
    public void test() {
        int length = 10000;
        int[] arr = new int[length];
        for (int i = 0; i < 10000; i++) {
            fillArr(arr);

            Pair<Integer, Integer> seqRes = Solution.getSequential(arr);
            Pair<Integer, Integer> blockingRes = Solution.getBlocking(arr);
            Pair<Integer, Integer> nonBlockingRes = Solution.getNonBlocking(arr);

            assertEquals(seqRes, blockingRes);
            assertEquals(seqRes, nonBlockingRes);
        }
    }
}