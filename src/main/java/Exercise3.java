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

    private static void populateList(LockfreeConcurrentSkipListSet list, boolean normal) {
        List<Integer> population;

        if(normal){
            population = Populator.normal();
        }else{
            population = Populator.uniform();
        }

        for (int i = 0; i < population.size(); i++) {
            list.add(population.get(i));
        }
    }

    private static long arraySum (List<Long> array){
        long sum = 0;
        for (int i = 0 ; i < array.size();i++){
            sum += array.get(i);
        }

        return sum;
    }
}


//import java.util.concurrent.ExecutionException;
//        import java.util.concurrent.ExecutorService;
//        import java.util.concurrent.Executors;
//        import java.util.concurrent.Future;
//
//public class ArraySum {
//
//    public static int sum(int[] a) {
//        // create a pool of threads
//        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//        int result = recSum(a, 0, a.length, service);
//        service.shutdown();
//
//        return result;
//    }
//
//    private static int recSum(int[] a, int start, int end, ExecutorService service) {
//        // base case
//        if (end == start) return 0;
//        if (end - start == 1) return a[start];
//
//        // recursion
//        int mid = (start / 2) + ((end + 1) / 2);
//        Future<Integer> lFuture = service.submit(
//                () -> {
//                    return recSum(a, start, mid, service);
//                }
//        );
//        Future<Integer> rFuture = service.submit(
//                () -> {
//                    return recSum(a, mid, end, service);
//                }
//        );
//
//        try {
//            return lFuture.get() + rFuture.get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//}

