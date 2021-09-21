import java.util.Arrays;
import java.util.Random;

public class Comparison {

    public static void main(String[] args) {
        Random r = new Random();

        for (int numberOfThreads = 3; numberOfThreads <= Runtime.getRuntime().availableProcessors(); numberOfThreads++) {
            QuickSort.threads = numberOfThreads;
            QuickSortExecutor.threads = numberOfThreads;
            QuickSortForks.threads = numberOfThreads;
            QuickSortStreams.threads = numberOfThreads;

            StringBuilder results = new StringBuilder();

            results.append(String.format("%-3s", "N")).append(String.format("%-10s", "Size")).append(String.format("%-15s", "Algorithm")).append(String.format("%-15s", "Time")).append('\n');
            results.append(new String(new char[43]).replace("\0", "=")).append("\n");

            int[][] unsorted = new int[][]{
                    new int[1000],
                    new int[10000],
                    new int[100000],
                    new int[1000000],
                    new int[10000000]
            };

            for (int[] array : unsorted) {
                for (int i = 0; i < array.length; i++) {
                    array[i] = r.nextInt(Integer.MAX_VALUE);
                }
            }

            int[][] sorted = new int[unsorted.length][];
            for (int i = 0; i < unsorted.length; i++) {
                sorted[i] = unsorted[i].clone();
                Arrays.sort(sorted[i]);
            }

            for (int i = 0; i < unsorted.length; i++) {
                long start, end;

                int[] temp = unsorted[i].clone();
                assert !Arrays.equals(temp, sorted[i]);

                start = System.nanoTime();
                QuickSort.quicksortSequential(temp);
                end = System.nanoTime();

                System.out.println(String.format("%-3s", numberOfThreads) + String.format("%-10s", temp.length) + String.format("%-15s", "Sequential") + String.format("%-15s", end - start));

                temp = unsorted[i].clone();
                assert !Arrays.equals(temp, sorted[i]);

                start = System.nanoTime();
                QuickSortExecutor.quicksortExecutor(temp);
                end = System.nanoTime();

                System.out.println(String.format("%-3s", numberOfThreads) + String.format("%-10s", temp.length) + String.format("%-15s", "Executor") + String.format("%-15s", end - start));

                temp = unsorted[i].clone();
                assert !Arrays.equals(temp, sorted[i]);

                start = System.nanoTime();
                QuickSortForks.quickSortForks(temp);
                end = System.nanoTime();

                System.out.println(String.format("%-3s", numberOfThreads) + String.format("%-10s", temp.length) + String.format("%-15s", "Forks") + String.format("%-15s", end - start));

                temp = unsorted[i].clone();
                assert !Arrays.equals(temp, sorted[i]);

                try {
                    start = System.nanoTime();
                    QuickSortStreams.quicksortStream(temp);
                    end = System.nanoTime();
                    System.out.println(String.format("%-3s", numberOfThreads) + String.format("%-10s", temp.length) + String.format("%-15s", "Streams") + String.format("%-15s", end - start));
                } catch (StackOverflowError e) {
                    results.append(String.format("%-3s", numberOfThreads)).append(String.format("%-10s", temp.length)).append(String.format("%-15s", "Streams")).append(String.format("%-15s", "Stackoverflow")).append('\n');
                }

//                System.out.println(results.toString());
//                System.exit(0);
            }
            System.out.println(results.toString());
        }

    }
}
