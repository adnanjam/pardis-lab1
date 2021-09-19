import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {

    int[] array1 = {1, 21, 132, 400, 50000000};
    int[] array2 = {1, 21, 132, 400, 50000000};
    int[] array3 = {400, 50000000, 21, 1, 132};
    int[] array4 = {50000000, 400, 132, 21, 1};

    int n = 1000000;
    int[] arrayExtreme = new int[n];
    int[] arrayExtremeSorted = new int[n];

    @BeforeEach
    void initAll() {

        sortedOdd = new int[]{1, 21, 132, 400, 50000000};
        unsortedOdd = new int[]{400, 50000000, 21, 1, 132};
        reversedOdd = new int[]{50000000, 400, 132, 21, 1};

        sortedEven = new int[]{21, 132, 400, 50000000};
        unsortedEven = new int[]{400, 50000000, 21, 132};
        reversedEven = new int[]{50000000, 400, 132, 21};

        Random r = new Random();
        for (int i = 0; i < n; i++) {
            arrayExtreme[i] = r.nextInt(Integer.MAX_VALUE);
        }
        arrayExtremeSorted = arrayExtreme.clone();
        Arrays.sort(arrayExtremeSorted);
    }

    @Test
    void testSequentialSorted() {
        QuickSort.quicksortSequential(array2);
        assertArrayEquals(array1, array2);
    }

    @Test
    void testSequentialUnsorted() {
        QuickSort.quicksortSequential(array3);
        assertArrayEquals(array1, array3);
    }

    @Test
    void testSequentialSortedDescending() {
        QuickSort.quicksortSequential(array4);
        assertArrayEquals(array1, array4);
    }

    @Test
    void testSequentialExtreme() {
        QuickSort.quicksortSequential(arrayExtreme);
        assertArrayEquals(arrayExtremeSorted, arrayExtreme);
    }

}