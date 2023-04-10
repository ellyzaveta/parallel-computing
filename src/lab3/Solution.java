package lab3;

import org.testng.internal.collections.Pair;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Solution {

    public static Pair<Integer, Integer> getSequential(int[] arr) {
        int diffOdd = 0;
        int minOdd = Integer.MAX_VALUE;

        for (int cur : arr) {
            if (cur % 2 != 0) {
                diffOdd -= cur;
                minOdd = Math.min(minOdd, cur);
            }
        }

        return new Pair<>(diffOdd, minOdd);
    }

    public static Pair<Integer, Integer> getBlocking(int[] arr) {
        BlockingResult result = new BlockingResult();

        Arrays.stream(arr).parallel()
                .forEach(cur -> {
                    if (cur % 2 != 0) result.add(cur);
                });

        return result.get();
    }

    public static Pair<Integer, Integer> getNonBlocking(int[] arr) {
        AtomicInteger diffOdd = new AtomicInteger(0);
        AtomicInteger minOdd = new AtomicInteger(Integer.MAX_VALUE);

        Arrays.stream(arr).parallel()
                .forEach(cur -> {
                    if (cur % 2 != 0) {
                        int oldValue, newValue;

                        do {
                            oldValue = diffOdd.get();
                            newValue = oldValue - cur;
                        } while (!diffOdd.compareAndSet(oldValue, newValue));

                        do {
                            oldValue = minOdd.get();
                            newValue = Math.min(oldValue, cur);
                        } while (!minOdd.compareAndSet(oldValue, newValue));
                    }
                });

        return new Pair<>(diffOdd.get(), minOdd.get());
    }

}
