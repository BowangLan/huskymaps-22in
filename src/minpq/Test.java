package minpq;

import java.util.*;

class Test {
    public static void main(String[] args) {
        System.out.println("Start minpq testing...");

        //ExtrinsicMinPQ<String> pq = new DoubleMapMinPQ<>();
        //ExtrinsicMinPQ<String> pq = new UnsortedArrayMinPQ<>();
        //ExtrinsicMinPQ<String> pq = new HeapMinPQ<>();
        ExtrinsicMinPQ<String> pq = new OptimizedHeapMinPQ<>();
        pq.add("1", 1.0);
        pq.add("2", 2.0);
        pq.add("3", 3.0);
        pq.add("4", 4.0);
        pq.add("5", 5.0);
        pq.add("6", 6.0);
        pq.add("11", 11.0);
        pq.add("12", 12.0);
        System.out.println("peekMin: " + pq.peekMin());
        pq.add("0", 0.0);
        System.out.println("peekMin: " + pq.peekMin());
        pq.add("-1", -0.1);
        System.out.println("peekMin: " + pq.peekMin());

        pq.removeMin();
        pq.removeMin();
        pq.removeMin();

        // Call methods to evaluate behavior.
        pq.changePriority("3", 0.1);
        while (!pq.isEmpty()) {
            System.out.print(pq.removeMin());
            System.out.print(" ");
        }

    }

}
