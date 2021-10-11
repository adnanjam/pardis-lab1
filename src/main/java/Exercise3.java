import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Exercise3 {
    final static int MAX_OP = 1_000_000;
    final static int TOTAL_RUNS = 10;
    final static int[] THREAD_COUNTS = new int[]{2,12,30,46};
    final static double addProb = 0.1;
    final static double removeProb = 0.2;
    static Random r = new Random();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LockfreeConcurrentSkipListSet<Integer> list = new LockfreeConcurrentSkipListSet<>();
        populateList(list, false); // Fills the list with elements

        for(int threadCount : THREAD_COUNTS) {
            // Average over ten runs
            List<Long> results = new ArrayList<>(TOTAL_RUNS);
            for(int run = 0; run < TOTAL_RUNS; run ++){
                long start = System.nanoTime();
                ExecutorService service = Executors.newFixedThreadPool(threadCount);

                for (int i = 0; i < MAX_OP; i++) {
                    double p = r.nextDouble();

                    if (p > removeProb) {
                        // contains
                        service.submit(() -> list.contains(r.nextInt()));
                    } else if (p > addProb) {
                        // remove
                        service.submit(() -> list.remove(r.nextInt()));
                    } else {
                        // add
                        service.submit(() -> list.add(r.nextInt()));
                    }
                }
                service.shutdown();
                results.add(System.nanoTime() - start);
            }

            long avgExecutionTime = arraySum(results) / TOTAL_RUNS;

            System.out.println( threadCount + ",  " + avgExecutionTime + ", " + avgExecutionTime / 1_000_000);

        }
    }
}
