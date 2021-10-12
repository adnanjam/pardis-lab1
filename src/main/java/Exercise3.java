import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Exercise3 {
    final static int MAX_OP = 1_000_00;
    final static int TOTAL_RUNS = 10;
    final static int[] THREAD_COUNTS = new int[] { 2, 12, 30, 46 };
    final static double addProb = 0.1;
    final static double removeProb = 0.2;
    static Random r = new Random();

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        for (int normal = 0; normal < 2; normal++) {

            for (int threadCount : THREAD_COUNTS) {
                LockfreeConcurrentSkipListSet_6<Integer> list = new LockfreeConcurrentSkipListSet_6<>();

                populateList(list, normal); // Fills the list with elements

                // Average over ten runs
                List<Long> results = new ArrayList<>(TOTAL_RUNS);
                for (int run = 0; run < TOTAL_RUNS; run++) {
                    long start = System.nanoTime();
                    ExecutorService service = Executors.newFixedThreadPool(threadCount);

                    for (int i = 0; i < MAX_OP; i++) {
                        double p = r.nextDouble();

                        if (p > removeProb) { // larger than removeProb
                            // contains
                            service.execute(() -> list.contains(r.nextInt()));
                        } else if (p > addProb) { // Smaller than removeProb and larger than addProb
                            // remove
                            service.execute(() -> list.remove(r.nextInt()));
                        } else { // smaller than addProb and larger than 0
                            // add
                            service.execute(() -> list.add(r.nextInt()));
                        }
                    }

                    service.shutdown();
                    while (true) {
                        try {
                            if (service.awaitTermination(500, TimeUnit.MICROSECONDS)) {
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    results.add(System.nanoTime() - start);
                }

                long avgExecutionTime = arraySum(results) / TOTAL_RUNS;

                System.out.println(
                        threadCount + ",  " + avgExecutionTime + ", " + normal + ", " + +avgExecutionTime / 1_000_000);
            }
        }
    }

    private static void populateList(LockfreeConcurrentSkipListSet_6 list, int normal) {
        List<Integer> population;

        if (normal == 1) {
            population = Populator.normal();
        } else {
            population = Populator.uniform();
        }

        for (int i = 0; i < population.size(); i++) {
            list.add(population.get(i));
        }
    }

    private static long arraySum(List<Long> array) {
        long sum = 0;
        for (int i = 0; i < array.size(); i++) {
            sum += array.get(i);
        }

        return sum;
    }
}
