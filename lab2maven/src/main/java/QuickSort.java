import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickSort {
    public static void main(String[] args) {
        int[] array = generateArray(100, 100000);
        int[] clone = array.clone();


        quicksortSequential(array);
        Arrays.sort(clone);

        if (Arrays.equals(array, clone)) {
            System.out.println("Correct");
        } else {
            System.out.println("Incorrect");
        }

    }

    public static void quicksortSequential(int[] array){
        quicksortSequentialRecursion(array, 0, array.length - 1);
    }



    private static void quicksortSequentialRecursion(int[] array, int low, int high) {

        if (low >= 0 && high >= 0 && low < high) {
            int p = partition(array, low, high);
            if (p-1-low < (p+1) - high) {
                quicksortSequentialRecursion(array, low, p - 1); // left side
                quicksortSequentialRecursion(array, p + 1, high); // right side
            } else {
                quicksortSequentialRecursion(array, p + 1, high); // right side
                quicksortSequentialRecursion(array, low, p - 1); // left side
            }
        }
    }

    public static int partition(int[] array, int low, int high) {

        int pivot = array[high]; // pivot must be the last element
        int i = low - 1; // pivot index

        int temp;
        for (int j = low; j <= high; j++) {
            // If current element is less than or equal to pivot
            if (array[j] < pivot) {
                i++; // move the pivot index forward
                // swap the current element with the element at the pivot:
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }

        }

        temp = array[i + 1];

        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    private static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + (i == array.length - 1 ? "\n" : ", "));

        }
    }

    public static int[] generateArray(int n, int max) {
        Random random = new Random();

        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(max);
        }

        return array;
    }
}
