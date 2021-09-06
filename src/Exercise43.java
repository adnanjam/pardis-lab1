import java.util.ArrayList;

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
                    p::eat
            ).start();
        }
    }

    public static class Philosopher {
        Philosopher left_neighbour;
        Philosopher right_neighbour;
        boolean myChopstick = false;
        boolean myChopstickIsLent = false;
        boolean otherChopstick = false;

        public void eat() {

        }

        public void getMyChopstick() {

        }

        public void releaseMyChopstick() {

        }

        public void getOtherChopstick() {

        }

        public void releaseOtherChopstick() {

        }

        public void lendMyChopstick() {

        }

        public void getBackMyChopstick() {

        }

    }

}
