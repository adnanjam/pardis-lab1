import java.util.ArrayList;
import java.util.Random;


/**
 * In our example we make a box object as a resource that uses a semaphore, and then we execute multiple threads
 * trying to access the box object simultaneously.
 */
public class Exercise42 {
    /**
     * Authors: Adnan Jamil Ahsan, Hovig Manjikian
     * Date: 2021-09-07
     * Lab 1 - DD2443
     */
    public static void main(String[] args) throws InterruptedException {
        Box box = new Box(2);

        Runnable user = () -> {
            for (int i = 0; i < 5; i++) {
                try {
                    box.getIn();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t1 = new Thread(user);
        Thread t2 = new Thread(user);
        Thread t3 = new Thread(user);
        Thread t4 = new Thread(user);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

    }

    public static class CountingSemaphore {
        private int counter;

        public CountingSemaphore(int amount) {
            this.counter = amount;
        }

        public synchronized void sSignal() {
            counter++;
            this.notify();
        }

        public synchronized void sWait() throws InterruptedException {
            while (counter <= 0) {
                this.wait();
            }
            counter--;
        }
    }

    public static class Box {
        public int size;
        private CountingSemaphore semaphore;
        private ArrayList<String> threadsInside;
        private Random r = new Random();

        public Box(int size) {
            this.size = size;
            this.semaphore = new CountingSemaphore(size);
            this.threadsInside = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                threadsInside.add("");
            }
        }

        public void getIn() throws InterruptedException {
            semaphore.sWait();
            for (int i = 0; i < size; i++) {
                if (threadsInside.get(i).equals("")) {
                    threadsInside.set(i, Thread.currentThread().getName());
                    System.out.println(threadsInside + " are inside the box.");
                    Thread.sleep(r.nextInt(1000) + 500);
                    threadsInside.set(i, "");
                    break;
                }
            }
            semaphore.sSignal();
        }
    }
}
