package lab1;

import org.junit.jupiter.api.Test;
import utils.File;

import static org.junit.jupiter.api.Assertions.*;

public class StringReverseTest {

    @Test
    public void sequentialSolution() {
        char[] str = File.getCharArr("3");

        StringBuilder expectedReverseStr = new StringBuilder(String.valueOf(str));
        expectedReverseStr.reverse();

        String actualReverseStr = String.valueOf(StringReverse.sequentialSolution(str));

        assertEquals(expectedReverseStr.toString(), actualReverseStr);
    }

    @Test
    public void parallelSolution() throws InterruptedException {
        char[] str = File.getCharArr("3");

        StringBuilder expectedReverseStr = new StringBuilder(String.valueOf(str));
        expectedReverseStr.reverse();

        String actualReverseStr = String.valueOf(StringReverse.parallelSolution(str, 8));

        assertEquals(expectedReverseStr.toString(), actualReverseStr);
    }

    @Test
    public void solutionsComparing() throws InterruptedException {
        for(int i = 0; i < 9; i++) {
            char[] str = File.getCharArr(String.valueOf(i));
            char[] sequentialSolution = StringReverse.sequentialSolution(str);
            char[] parallelSolution = StringReverse.parallelSolution(str, 8);
            assertArrayEquals(sequentialSolution, parallelSolution);
        }
    }
}