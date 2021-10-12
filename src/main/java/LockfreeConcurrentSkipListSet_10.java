import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockfreeConcurrentSkipListSet_10<T> {
    public int taskNumber = 0;
    ArrayBlockingQueue logQ;

    static final int MAX_LEVEL = 16;
    final Node<T> head = new Node<T>(Integer.MIN_VALUE);
    final Node<T> tail = new Node<T>(Integer.MAX_VALUE);

    public LockfreeConcurrentSkipListSet_10(ArrayBlockingQueue logQ) {
        this.logQ  = logQ;
        for (int i = 0; i < head.next.length; i++) {
            head.next[i] = new AtomicMarkableReference<LockfreeConcurrentSkipListSet_10.Node<T>>(tail, false);
        }

    }

    boolean add(T x) throws InterruptedException {

        //        if (taskNumber == 4) {
        //            logQ.put( new String[]{String.valueOf(Thread.currentThread().getId()),  "ADD", String.valueOf(System.nanoTime())});
        //        }

        int topLevel = randomLevel();
        int bottomLevel = 0;

        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1]; // list of predecessors
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1]; // list of successors

        while (true) {

            boolean found = find(x, preds, succs);

            // If the element already exists

            if (found) {
                if (taskNumber == 4) {
                    logQ.put(new String[]{String.valueOf(Thread.currentThread().getId()),  "LIN-ADD-FALSE", String.valueOf(System.nanoTime())});
                }
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

                if (taskNumber == 4) {
                    logQ.put(new String[]{String.valueOf(Thread.currentThread().getId()),  "LIN-ADD-TRUE", String.valueOf(System.nanoTime())});
                }
                return true;
            }
        }
    }

    // Method to remove a node
    boolean remove(T x) throws InterruptedException{
//        logs.append(Thread.currentThread().getName() + ", REMOVE, " + System.nanoTime() + ", ");
        int bottomLevel = 0;
        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
        Node<T> succ;

        while (true) {
            boolean found = find(x, preds, succs);
            if (!found) {
//                if (taskNumber == 4) {
////                    logs.append("LIN_REM, " + System.nanoTime() + ", ");
//                }
//                logs.append("RET, false, " + System.nanoTime() + "\n");
                    logQ.put(new String[]{String.valueOf(Thread.currentThread().getId()),  "LIN-REMOVE-FALSE", String.valueOf(System.nanoTime())});

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
                while (true) {
                    boolean iMarkedIt = nodeToRemove.next[bottomLevel].compareAndSet(succ, succ, false, true);
                    succ = succs[bottomLevel].next[bottomLevel].get(marked);

//                    logs.append("LIN_REM, " + System.nanoTime() + ", ");

                    logQ.put(new String[]{String.valueOf(Thread.currentThread().getId()),  "LIN-REMOVE-TRUE", String.valueOf(System.nanoTime())});

                    if (iMarkedIt) {
                        find(x, preds, succs);
//                        logs.append("RET, true, " + System.nanoTime() + "\n");
                        return true;
                    } else if (marked[0]) {
//                        logs.append("RET, false, " + System.nanoTime() + "\n");
                        return false;
                    }
                }
            }
        }

    }

    boolean contains(T x) throws InterruptedException{
//        logs.append(Thread.currentThread().getName() + ", CONTAINS, " + System.nanoTime() + ", ");

        int bottomLevel = 0;
        int v = x.hashCode();
        boolean[] marked = {false};
        Node<T> pred = head, curr = null, succ = null;

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


        if (curr.key == v) {
            if (taskNumber == 4) {
//                logs.append("LIN_CON, " + System.nanoTime() + ", ");
                logQ.put(new String[]{String.valueOf(Thread.currentThread().getId()),  "LIN-CONTAINS-TRUE", String.valueOf(System.nanoTime())});

            }

//            logs.append("RET, true, " + System.nanoTime() + "\n");
            return true;
        } else {
            if (taskNumber == 4) {
                logQ.put(new String[]{String.valueOf(Thread.currentThread().getId()),  "LIN-CONTAINS-FALSE", String.valueOf(System.nanoTime())});
            }

//            logs.append("RET, false, " + System.nanoTime() + "\n");
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
