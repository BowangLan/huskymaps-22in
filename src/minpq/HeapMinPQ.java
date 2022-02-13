package minpq;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * {@link PriorityQueue} implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class HeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link PriorityQueue} storing {@link PriorityNode} objects representing each item-priority pair.
     */
    private final PriorityQueue<PriorityNode<T>> pq;

    /**
     * Constructs an empty instance.
     */
    public HeapMinPQ() {
        pq = new PriorityQueue<>(Comparator.comparingDouble(PriorityNode::priority));
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        this.pq.add(new PriorityNode<T>(item, priority));
    }

    @Override
    public boolean contains(T item) {
        return this.pq.contains(new PriorityNode<T>(item, 1.0));
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return this.pq.peek().item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        //PriorityNode<T> node = new PriorityNode<T>(this.pq.peek().item(), 1.0);
        //this.pq.remove(node);
        //return node.item();
        return this.pq.poll().item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        PriorityNode<T> node = new PriorityNode<T>(item, 1.0);
        this.pq.remove(node);
        this.pq.add(new PriorityNode<T>(item, priority));
    }

    @Override
    public int size() {
        return this.pq.size();
    }
}
