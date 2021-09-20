import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuickSortStreams {

    public static void quicksortStream(int[] array) {
        ArrayList<Integer> list = new ArrayList<>(array.length);
        for (int j : array) list.add(j);

        List<Integer> sorted = quicksortStreamRecursion(list, array[0]/3 + array[array.length / 2]/3 + array[array.length - 1] / 3);

        for (int i = 0; i < array.length; i++) {
            array[i] = sorted.get(i);
        }
    }


    private static List<Integer> quicksortStreamRecursion(List<Integer> list, int pivot) {
        List<Integer> lArray = list.stream().parallel().filter(x -> x < pivot).collect(Collectors.toList());
        List<Integer> rArray = list.stream().parallel().filter(x -> x >= pivot).collect(Collectors.toList());

        List<Integer> result = lArray.size() <= 1 ? lArray : quicksortStreamRecursion(lArray, lArray.get(0)/3 + lArray.get(lArray.size() / 2)/3 + lArray.get(lArray.size() - 1)/3);
        result.addAll(rArray.size() <= 1 ? rArray : quicksortStreamRecursion(rArray, rArray.get(0)/3 + rArray.get(rArray.size() / 2)/3 + rArray.get(rArray.size() - 1) / 3));

        return result;
    }
}
