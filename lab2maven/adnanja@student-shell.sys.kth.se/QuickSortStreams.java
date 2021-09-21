import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class QuickSortStreams {

    public static Integer threads = Runtime.getRuntime().availableProcessors();;

    public static void quicksortStream(int[] array) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(threads);

        ArrayList<Integer> list = new ArrayList<>(array.length);
        for (int j : array) list.add(j);

        List<Integer> sorted = null;
        try {
            sorted = quicksortStreamRecursion(list, array[0] / 3 + array[array.length / 2] / 3 + array[array.length - 1] / 3, forkJoinPool);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < array.length; i++) {
            array[i] = sorted.get(i);
        }
    }

    private static List<Integer> quicksortStreamRecursion(List<Integer> list, int pivot, ForkJoinPool pool) throws ExecutionException, InterruptedException {
        final List<Integer> lArray = pool.submit(() -> list.stream().parallel().filter(x -> x < pivot).collect(Collectors.toList())).get();
        final List<Integer> rArray = pool.submit(() -> list.stream().parallel().filter(x -> x >= pivot).collect(Collectors.toList())).get();

        List<Integer> lResult;
        List<Integer> rResult;

        if (lArray.size() < rArray.size()) {
            lResult = lArray.size() <= 1 ? lArray : quicksortStreamRecursion(lArray, lArray.get(0) / 3 + lArray.get(lArray.size() / 2) / 3 + lArray.get(lArray.size() - 1) / 3, pool);
            rResult = rArray.size() <= 1 ? rArray : quicksortStreamRecursion(rArray, rArray.get(0) / 3 + rArray.get(rArray.size() / 2) / 3 + rArray.get(rArray.size() - 1) / 3, pool);
        } else {
            rResult = rArray.size() <= 1 ? rArray : quicksortStreamRecursion(rArray, rArray.get(0) / 3 + rArray.get(rArray.size() / 2) / 3 + rArray.get(rArray.size() - 1) / 3, pool);
            lResult = lArray.size() <= 1 ? lArray : quicksortStreamRecursion(lArray, lArray.get(0) / 3 + lArray.get(lArray.size() / 2) / 3 + lArray.get(lArray.size() - 1) / 3, pool);
        }

        List<Integer> result = new ArrayList<>(list.size());
        result.addAll(lResult);
        result.addAll(rResult);

        return result;
    }
}
