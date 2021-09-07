import java.util.ArrayList;
import java.util.Random;

/**
 * Argument for starvation freedom
 * ----------------------------------
 * Since the monitor objects will be released in the same order as they are blocked
 * it prevents the starvation. If we change to unblock order we will get starvation
 * on some threads.
 * Moreover, each thread can only hold to monitor objects for a limited time
 * after that it will release the
 */
public class Exercise43 {
    public static void main(String[] args) {
        int numberOfPhilosophers = 5;
        ArrayList<Philosopher> table = new ArrayList<>(numberOfPhilosophers);

        // Create a table with philosophers
        for (int i = 0; i < numberOfPhilosophers; i++) {
            table.add(new Philosopher());
        }

        // Set the neighbours of each philosopher
        for (int i = 0; i < numberOfPhilosophers; i++) {
            table.get(i).right_neighbour = table.get((i + 1) % numberOfPhilosophers);
        }

        // Let the philosophers start to eat in parallel
        for (int i = 0; i < numberOfPhilosophers; i++) {
            Philosopher p = table.get(i);
            new Thread(
                    p::eat, "Philosopher " + String.valueOf(i + 1)
            ).start();
        }
    }

    // We assume that our chopstick is the one on the left so only the neighbour on the right is relevant.
    public static class Philosopher {
        public Philosopher right_neighbour;
        private boolean chopstickIsAvailable = true;
        private final Object chopstickMonitor = new Object();
        private final Random r = new Random();

        public void eat() throws InterruptedException {
            while (true) {
                getChopstick();
                getNeighboursChopstick();
                System.out.println(Thread.currentThread().getName() + " is eating.");
                Thread.sleep(r.nextInt(1000));
                releaseChopstick();
                releaseNeighboursChopstick();
            }
        }

        public void getChopstick() throws InterruptedException {
            synchronized (chopstickMonitor) {
                while (!chopstickIsAvailable) {
                    chopstickMonitor.wait();
                }
                chopstickIsAvailable = false;
            }
        }

        public void releaseChopstick() {
            synchronized (chopstickMonitor) {
                chopstickIsAvailable = true;
                chopstickMonitor.notify();
            }
        }

        public void getNeighboursChopstick() throws InterruptedException {
            right_neighbour.getChopstick();
        }

        public void releaseOtherChopstick() {

        }
    }
}
