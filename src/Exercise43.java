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
            table.get(i).left_neighbour = table.get((i - 1) % numberOfPhilosophers);
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

    public static class Philosopher {
        Philosopher left_neighbour;
        Philosopher right_neighbour;
        boolean myChopstickIsAvailable = true;
        boolean myChopstickIsLent = false;
        boolean otherChopstick = false;
        final Object myChopstickMonitor = new Object();
        final Object otherChopstickMonitor = new Object();
        Random r = new Random();

        public void eat() throws InterruptedException {
            while (true) {
                getMyChopstick();
                getOtherChopstick();
                System.out.println(Thread.currentThread().getName() + " is eating.");
                Thread.sleep(r.nextInt(1000));
                releaseOtherChopstick();
                releaseMyChopstick();
            }
        }

        public void getMyChopstick() throws InterruptedException {
            synchronized (myChopstickMonitor) {
                while (myChopstickIsLent) {
                    myChopstickMonitor.wait();
                }
                myChopstickIsAvailable = false;
            }
        }

        public void releaseMyChopstick() {
            synchronized (myChopstickMonitor) {
                myChopstickIsAvailable = true;
                myChopstickMonitor.notify();
            }
        }

        public void getOtherChopstick() {
            Thread left = new Thread(
                    () -> {
                        left_neighbour.lendMyChopstick();
                    }
            );

            Thread right = new Thread(
                    () -> {
                        right_neighbour.lendMyChopstick();
                    }
            );

            left.start();
            right.start();

            synchronized (otherChopstickMonitor){

            }

        }

        public void releaseOtherChopstick() {

        }

        public void lendMyChopstick() {

        }

        public void getBackMyChopstick() {

        }

    }

}
