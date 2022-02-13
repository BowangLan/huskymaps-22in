package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class UnsortedArrayMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the item-priority pairs in no specific order.
     */
    private final List<PriorityNode<T>> items;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        items = new ArrayList<PriorityNode<T>>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        items.add(new PriorityNode<T>(item, priority));
    }

    @Override
    public boolean contains(T item) {
        for (PriorityNode<T> cur : items) {
            if (Objects.equals(cur.item(), item)) return true;
        }
        return false;
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<T> minNode = this.items.get(0);
        for (PriorityNode<T> cur : this.items) {
            if (cur.priority() < minNode.priority()) minNode = cur;
        }
        return minNode.item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<T> minNode = this.items.get(0);
        int minIndex = 0;
        for (int i = 1; i < this.items.size(); i++) {
            if (this.items.get(i).priority() < minNode.priority()) {
                minNode = this.items.get(i);
                minIndex = i;
            }
        }
        this.items.remove(minIndex);
        return minNode.item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        PriorityNode<T> node = this.items.get(0);
        for (PriorityNode<T> cur : this.items) {
            if (Objects.equals(cur.item(), item)) {
                cur.setPriority(priority);
                break;
            }
        }
    }

    @Override
    public int size() {
        return this.items.size();
    }
}
