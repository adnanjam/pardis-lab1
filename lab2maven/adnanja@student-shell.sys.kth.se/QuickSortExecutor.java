import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class QuickSortExecutor {
    private static final int maxThreads = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

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

    public static void quicksortExecutor(int[] array)  {
        quickSortExecutorRecursive(array, 0, array.length - 1, executor);
//         executor.shutdown();
        int timeout = 1;
        try {
            if (!executor.awaitTermination(timeout, TimeUnit.SECONDS)) {
                System.err.println("Threads didn't finish in "+timeout+" seconds!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void quickSortExecutorRecursive(int[] array, int low, int high, ExecutorService executor) {

        if (low >= 0 && high >= 0 && low < high) {
            int p = QuickSort.partition(array, low, high);

            // left side
            try {
                executor.execute(() -> quickSortExecutorRecursive(array, low, p - 1, executor));
                // right side
                executor.execute(() ->quickSortExecutorRecursive(array, p + 1, high, executor));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
