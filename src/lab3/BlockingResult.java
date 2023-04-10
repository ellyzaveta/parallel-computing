package lab3;

import org.testng.internal.collections.Pair;

public class BlockingResult {
    private int diffOdd;
    private int minOdd;

    public BlockingResult() {
        diffOdd = 0;
        minOdd = Integer.MAX_VALUE;
    }

    public synchronized void add(int value) {
        diffOdd -= value;
        minOdd = Math.min(minOdd, value);
    }

    public Pair<Integer, Integer> get() {
        return new Pair<>(diffOdd, minOdd);
    }
}
