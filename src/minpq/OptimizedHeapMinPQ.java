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
        //System.out.println("add: " + priority);
        this.checkHeap();
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
        double min1 = 1000;
        PriorityNode<T> minNode1 = this.items.get(0);
        for (PriorityNode<T> cur : this.items) {
            if (cur.priority() < min1) {
                min1 = cur.priority();
                minNode1 = cur;
            }
        }
        System.out.println("Min 1: " + min1);
        PriorityNode<T> min2 = this.items.get(0);
        System.out.println("Min 2: " + min2.priority());
        return minNode1.item();
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
        //this.inOrder();
        return node.item();
    }

    private void inOrder() {
        PriorityNode<T> cur = this.items.get(0);
        System.out.print(cur.priority());
        boolean shouldPrint = this.size() < 10;
        boolean order = true;
        for (int i = 1; i < this.items.size(); i++) {
            if (shouldPrint) {
                System.out.print(" " + cur.priority());
            }
            order = (cur.priority() <= this.items.get(i).priority()) && order;
            cur = this.items.get(i);
        }
        System.out.println("\n - in order: " + order);
    }

    private void checkHeap() {
        boolean valid = this._validBinaryHeap(0);
        String color = "\u001B[32m";
        System.out.println("valid heap: " + valid);
    }

    private boolean _validBinaryHeap(int cur) {
        if (!this.accessible(cur)) return true;
        boolean valid = true;
        int left = this.left(cur);
        int right = this.right(cur);
        PriorityNode<T> curNode = this.items.get(cur);
        if (this.accessible(left)) {
            if (this.items.get(left).priority() < curNode.priority()) return false;
            boolean leftValid = this._validBinaryHeap(left);
            if (!leftValid) return false;
        }
        if (this.accessible(right)) {
            if (this.items.get(right).priority() < curNode.priority()) return false;
            boolean rightValid = this._validBinaryHeap(right);
            if (!rightValid) return false;
        }
        return true;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        int i = this.itemToIndex.get(item);
        PriorityNode<T> node = this.items.get(i);
        this.items.remove(node);
        this.add(item, priority);
    }

    @Override
    public int size() {
        return this.items.size();
    }

    private int parent(int index) {
        return ((index + 1) / 2) - 1;
    }

    /** Returns the index of the given index's left child. */
    private static int left(int index) {
        return ((index + 1) * 2) - 1;
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
        //System.out.println("swim index " + index + " parent " + parent);
        //System.out.println("swim index " + this.items.get(index).priority() + " parent " + this.items.get(parent).priority());
        while (this.accessible(parent) && this.items.get(index).priority() > this.items.get(parent).priority()) {
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
            //if ((t1 == index && t2 == child) || (t2 == child && t1 == index)) {
                //System.out.println("repeat break");
                //break;
            //}
            this.swap(index, child);
            index = child;
            child = this.min(this.left(index), this.right(index));
            t1 = index;
            t2 = child;
        }
    }
    

}
