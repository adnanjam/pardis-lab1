public class Exercise3 {

    /**
     * Guarded blocks
     * 1. Write a short program in which one thread increments an integer 1,000,000 times,
     * and a second thread prints the integer
     * -- without waiting for it to finish.
     * 2. Modify the program to use a condition variable to signal completion
     * of the incrementing task by the first thread before the second thread prints the value.
     */

    public static void main(String[] args) throws InterruptedException {
        MyThread3 instance = new MyThread3();

        Thread t1 = new Thread(instance);
        t1.start();

        new Thread(() -> {
            try {
                instance.print();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
}

class MyThread3 implements Runnable {

    public int counter = 0;
    public boolean condition = false;

    public synchronized void run() {

        for (int i = 0; i < 100000000; i++) {
            increment();
        }
        condition = true;
        notifyAll();
    }

    public synchronized void print() throws InterruptedException {
        while (!condition) {
            wait();
        }

        System.out.println(counter);
    }

    public synchronized void increment() {
        counter++;
    }
}