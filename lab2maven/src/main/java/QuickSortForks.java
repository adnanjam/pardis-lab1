import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class QuickSortForks {

    public static Integer threads = Runtime.getRuntime().availableProcessors();

    public static void quickSortForks(int[] array) {

        QSFRT tasks = new QSFRT(array, 0, array.length - 1);
        ForkJoinPool forkJoinPool = new ForkJoinPool(threads);
        forkJoinPool.invoke(tasks);

    }

    private static class QSFRT extends RecursiveAction {

        int[] array;
        int high;
        int low;

        public QSFRT(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (low >= 0 && high >= 0 && low < high) {
                List<QSFRT> tasks = new ArrayList<>(2);
                int p = QuickSort.partition(array, low, high);

                // left side
                QSFRT left = new QSFRT(array, low, p - 1);
                left.fork();

                QSFRT right = new QSFRT(array, p + 1, high);
                right.fork();

                left.join();
                right.join();

            }
        }
    }
}
