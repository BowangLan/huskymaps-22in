package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link ExtrinsicMinPQ} interface.
 *
 * @param <T> the type of elements in this priority queue.
 * @see ExtrinsicMinPQ
 */
public class OptimizedHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of item-priority pairs.
     */
    private final List<PriorityNode<T>> items;
    /**
     * {@link Map} of each item to its associated index in the {@code items} heap.
     */
    private final Map<T, Integer> itemToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        items = new ArrayList<>();
        itemToIndex = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        PriorityNode<T> newNode = new PriorityNode<T>(item, priority);
        this.items.add(newNode);    
        this.swim(this.size() - 1);
        this.itemToIndex.put(item, this.items.indexOf(newNode));
        //System.out.println(items);
    }

    @Override
    public boolean contains(T item) {
        return this.items.contains(new PriorityNode<T>(item, 1.0));
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return this.items.get(0).item();
    }

    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<T> node = this.items.get(0);
        //System.out.println("removeMin node priority " + node.priority());
        //System.out.println("removeMin last node priority " + this.items.get(this.items.size() - 1).priority());
        this.swap(0, this.size() - 1);
        this.items.remove(node);
        this.itemToIndex.remove(node.item());
        if (!isEmpty()) this.sink(0);
        //System.out.println("after removeMin " + this.items);
        return node.item();
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        int i = this.itemToIndex.get(item);
        this.items.get(i).setPriority(priority);
        //System.out.println("after changePriority for node " + node + ": " + this.items);
    }

    @Override
    public int size() {
        return this.items.size();
    }

    private int parent(int index) {
        if (index % 2 != 0) return index / 2;
        else return (index - 1) / 2;
    }

    /** Returns the index of the given index's left child. */
    private static int left(int index) {
        return index * 2 + 1;
    }

    /** Returns the index of the given index's right child. */
    private static int right(int index) {
        return left(index) + 1;
    }

    /** Returns true if and only if the index is accessible. */
    private boolean accessible(int index) {
        return 0 <= index && index <= (size() - 1);
    }

    /** Returns the index with the lower priority, or 0 if neither is accessible. */
    private int min(int index1, int index2) {
        if (!accessible(index1) && !accessible(index2)) {
            return 0;
        } else if (accessible(index1) && (!accessible(index2)
                || items.get(index1).priority() < items.get(index2).priority())) {
            return index1;
        } else {
            return index2;
        }
    }

    /** Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        PriorityNode<T> temp = this.items.get(index1);
        this.items.set(index1, this.items.get(index2));
        this.items.set(index2, temp);
    }

    /** Bubbles up the node currently at the given index. */
    private void swim(int index) {
        if (index == 0) return;
        int parent = this.parent(index);
        while (this.accessible(parent) && this.items.get(index).priority() < this.items.get(parent).priority()) {
            this.swap(index, parent);
            index = parent;
            parent = this.parent(index);
        }
    }

    /** Bubbles down the node currently at the given index. */
    private void sink(int index) {
        int child = this.min(this.left(index), this.right(index));
        int t1 = 0;
        int t2 = 0;
        //System.out.print("sink index " + index);
        //System.out.print(" len " + size());
        //System.out.println(" priority " + this.items.get(index).priority());
        while (this.accessible(child) && child != 0 && this.items.get(index).priority() > this.items.get(child).priority()) {
            //System.out.println("swap index " + index + " and child " + child);
            this.swap(index, child);
            index = child;
            child = this.min(this.left(index), this.right(index));
        }
    }
    

}
