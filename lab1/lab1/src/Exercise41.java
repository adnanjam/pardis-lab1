import java.util.ArrayDeque;
import java.util.Queue;

public class Exercise41 {
    /**
     * Authors: Adnan Jamil Ahsan, Hovig Manjikian
     * Date: 2021-09-07
     * Lab 1 - DD2443
     */

    public static void main(String[] args) throws InterruptedException {

        MySynchronizedClass mySynchronizedClass = new MySynchronizedClass();

        // Producer
        Runnable producer = () -> {
            try {
                mySynchronizedClass.add();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        Thread prod1 = new Thread(producer);
        Thread prod2 = new Thread(producer);

        prod1.start();
        prod2.start();

        // Consumer
        Runnable consumer = () -> {
            try {
                mySynchronizedClass.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread con1 = new Thread(consumer);
        Thread con2 = new Thread(consumer);
        con1.start();
        con2.start();

        con1.join();
        con2.join();

    }

    public static class MySynchronizedClass {
        private final Queue<String> queue = new ArrayDeque<>(10);
        public final Object full = new Object();
        public final Object empty = new Object();

        public void add() throws InterruptedException {
            String name = Thread.currentThread().getName();
            for (int i = 0; i < 1000; i++) {
                synchronized (full) {
                    while (queue.size() == 10) {
                        full.wait();
                    }
                    queue.add(name + ": Task " + i);
                }
                synchronized (empty) {
                    empty.notify();
                }
            }
        }

        public void consume() throws InterruptedException {
            while (true) {
                synchronized (empty) {
                    while (queue.isEmpty()) {
                        empty.wait();
                    }
                    String result = queue.remove();
                    System.out.println(result);
                }
                synchronized (full) {
                    full.notifyAll();
                }
            }
        }
    }
}
