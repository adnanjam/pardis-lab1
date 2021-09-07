import java.util.ArrayList;
import java.util.Random;

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
                releaseNeighboursChopstick();
                releaseChopstick();
            }
        }

        public void getChopstick() throws InterruptedException {
            synchronized (chopstickMonitor) {
                while (!chopstickIsAvailable) {
                    chopstickMonitor.wait(r.nextInt(500));
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
