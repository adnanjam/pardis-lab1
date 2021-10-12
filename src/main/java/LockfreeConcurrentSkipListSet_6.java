import java.util.Random;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockfreeConcurrentSkipListSet_6<T> {
    public int taskNumber = 0;
    private final Lock reel = new ReentrantLock();

    static final int MAX_LEVEL = 16;
    final Node<T> head = new Node<T>(Integer.MIN_VALUE);
    final Node<T> tail = new Node<T>(Integer.MAX_VALUE);

    public LockfreeConcurrentSkipListSet_6() {
        for (int i = 0; i < head.next.length; i++) {
            head.next[i] = new AtomicMarkableReference<LockfreeConcurrentSkipListSet_6.Node<T>>(tail, false);
        }
    }

    public LockfreeConcurrentSkipListSet_6(int task) {
        this.taskNumber = task;
        for (int i = 0; i < head.next.length; i++) {
            head.next[i] = new AtomicMarkableReference<LockfreeConcurrentSkipListSet_6.Node<T>>(tail, false);
        }
    }

    boolean add(T x) {
        int topLevel = randomLevel();
        int bottomLevel = 0;

        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1]; // list of predecessors
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1]; // list of successors

        reel.lock();

        while (true) {
            boolean found = find(x, preds, succs);

            // If the element already exists
            if (found) {

                if (taskNumber == 4) {
                    System.out.println(System.nanoTime() + ", " + Thread.currentThread().getName() + ", [ADD] FAILED, "
                            + x + " exists in the list.");
                }
                reel.unlock();
                return false;
            } else {
                // Prepare the new node
                Node<T> newNode = new Node<T>(x, topLevel);
                for (int level = bottomLevel; level <= topLevel; level++) {
                    Node<T> succ = succs[level]; // Take the successor node of the current level
                    newNode.next[level].set(succ, false);
                }

                Node<T> pred = preds[bottomLevel];
                Node<T> succ = succs[bottomLevel];

                if (!pred.next[bottomLevel].compareAndSet(succ, newNode, false, false)) {
                    continue;
                }

                for (int level = bottomLevel + 1; level <= topLevel; level++) {
                    while (true) {
                        pred = preds[level];
                        succ = succs[level];
                        if (pred.next[level].compareAndSet(succ, newNode, false, false)) {
                            break;
                        }

                        find(x, preds, succs);
                    }
                }

                System.out.println(System.nanoTime() + ", " + Thread.currentThread().getName() + ", [ADD] SUCCEEDED, "
                        + x + " added to the list.");

                reel.unlock();
                return true;
            }
        }
    }

    // Method to remove a node
    boolean remove(T x) {
        int bottomLevel = 0;
        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
        Node<T> succ;

        while (true) {
            boolean found = find(x, preds, succs);
            if (!found) {
                if (taskNumber == 4) {
                    System.out.println(System.nanoTime() + ", " + Thread.currentThread().getName()
                            + ", [REMOVE] FAILED, " + x + " does not exist in the list.");
                }
                return false;
            } else {
                Node<T> nodeToRemove = succs[bottomLevel];
                for (int level = nodeToRemove.topLevel; level >= bottomLevel + 1; level--) {
                    boolean[] marked = {false};
                    succ = nodeToRemove.next[level].get(marked);
                    while (!marked[0]) {
                        nodeToRemove.next[level].compareAndSet(succ, succ, false, true);
                        succ = nodeToRemove.next[level].get(marked);
                    }
                }

                boolean[] marked = {false};

                succ = nodeToRemove.next[bottomLevel].get(marked);
                // lock.lock();
                while (true) {
                    boolean iMarkedIt = nodeToRemove.next[bottomLevel].compareAndSet(succ, succ, false, true);
                    succ = succs[bottomLevel].next[bottomLevel].get(marked);
                    if (taskNumber == 4) {
                        System.out.println(System.nanoTime() + ", " + Thread.currentThread().getName()
                                + ", [REMOVE] SUCCEEDED, " + x + " is removed form the list.");
                    }

                    if (iMarkedIt) {
                        find(x, preds, succs);
                        // lock.unlock();
                        return true;
                    } else if (marked[0]) {
                        // lock.unlock();
                        return false;
                    }
                }
            }
        }

    }

    boolean contains(T x) {
        int bottomLevel = 0;
        int v = x.hashCode();
        boolean[] marked = {false};
        Node<T> pred = head, curr = null, succ = null;

        reel.lock();
        try {
            for (int level = MAX_LEVEL; level >= bottomLevel; level--) {
                curr = pred.next[level].getReference();
                while (true) {
                    succ = curr.next[level].get(marked);
                    while (marked[0]) {
                        curr = pred.next[level].getReference();
                        succ = curr.next[level].get(marked);
                    }
                    if (curr.key < v) {
                        pred = curr;
                        curr = succ;
                    } else {
                        break;
                    }
                }
            }
        } finally {
            reel.unlock();
        }

        if (curr.key == v) {
            if (taskNumber == 4) {
                System.out.println(System.nanoTime() + ", " + Thread.currentThread().getName()
                        + ", [CONTAINS] SUCCEEDED, " + x + " exists in the list.");
            }

            return true;
        } else {
            if (taskNumber == 4) {
                System.out.println(System.nanoTime() + ", " + Thread.currentThread().getName() + ", [CONTAINS] FAILED, "
                        + x + " does not exist in the list.");
            }

            return false;
        }

    }

    boolean find(T x, Node<T>[] preds, Node<T>[] succs) {
        int bottomLevel = 0;
        int key = x.hashCode();

        boolean[] marked = {false};
        boolean snip;

        Node<T> pred = null, curr = null, succ = null;

        retry:
        while (true) {
            pred = head;
            for (int level = MAX_LEVEL; level >= bottomLevel; level--) {
                curr = pred.next[level].getReference();
                while (true) {
                    succ = curr.next[level].get(marked);
                    while (marked[0]) {
                        snip = pred.next[level].weakCompareAndSet(curr, succ, false, false);
                        if (!snip)
                            continue retry;

                        curr = pred.next[level].getReference();
                        succ = curr.next[level].get(marked);
                    }
                    if (curr.key < key) {
                        pred = curr;
                        curr = succ;

                    } else {
                        break;
                    }
                }
                preds[level] = pred;
                succs[level] = curr;
            }
            return (curr.key == key);
        }

    }

    int randomLevel() {
        Random r = new Random();
        return r.nextInt(MAX_LEVEL);
    }

    public static final class Node<T> {
        final T value;
        final int key;
        final AtomicMarkableReference<Node<T>>[] next; // a list for the successors
        private int topLevel;

        // constructor for sentinel nodes (head, tail)
        public Node(int key) {
            value = null;
            this.key = key;
            next = (AtomicMarkableReference<Node<T>>[]) new AtomicMarkableReference[MAX_LEVEL + 1];
            for (int i = 0; i < next.length; i++) {
                next[i] = new AtomicMarkableReference<Node<T>>(null, false);
            }
            topLevel = MAX_LEVEL;
        }

        // constructor for ordinary nodes

        public Node(T x, int height) {
            value = x;
            key = x.hashCode();
            next = (AtomicMarkableReference<Node<T>>[]) new AtomicMarkableReference[MAX_LEVEL + 1];
            for (int i = 0; i < next.length; i++) {
                next[i] = new AtomicMarkableReference<Node<T>>(null, false);
            }

            topLevel = height;
        }
    }
    // Method to add a node

}
