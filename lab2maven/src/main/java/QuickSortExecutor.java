import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class QuickSortExecutor {
    public static Integer threads = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        int[] array = QuickSort.generateArray(100, Integer.MAX_VALUE);
        int[] clone = array.clone();

        quicksortExecutor(array);
        Arrays.sort(clone);

        if (Arrays.equals(array, clone)) {
            System.out.println("Correct");
        } else {
            System.out.println("Incorrect");
        }

    }

    public static void quicksortExecutor(int[] array) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future> futures = new ArrayList<>();

        quickSortExecutorRecursive(array, 0, array.length - 1, executor, futures);

        while (!futures.isEmpty()) {
            Future topFeature = futures.remove(0);
            try {
                if (topFeature != null) topFeature.get();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } catch (ExecutionException ie) {
                ie.printStackTrace();
            }
        }

        futures = null;
     }

    private static void quickSortExecutorRecursive(int[] array, int low, int high, ExecutorService executor, List futures) {

        if (low >= 0 && high >= 0 && low < high) {
            int p = QuickSort.partition(array, low, high);

            // left side
            try {


                Future leftFuture = (Future) executor.submit(() ->
                        quickSortExecutorRecursive(array, low, p - 1, executor, futures)
                );


                Future rightFuture = (Future) executor.submit(() ->
                        quickSortExecutorRecursive(array, p + 1, high, executor, futures)
                );


                futures.add(leftFuture);
                futures.add(rightFuture);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}
