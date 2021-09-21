import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuickSortTestByAlgorithm {

    int[] resultOdd = {1, 21, 132, 400, 50000000};
    int[] sortedOdd = new int[5];
    int[] unsortedOdd = new int[5];
    int[] reversedOdd = new int[5];

    int[] resultEven = {21, 132, 400, 50000000};
    int[] sortedEven = new int[4];
    int[] unsortedEven = new int[4];
    int[] reversedEven = new int[4];

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

    @Nested
    @DisplayName("Sequential")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Sequential {

        @Test
        @Order(1)
        void sequentialSortedOdd() {
            QuickSort.quicksortSequential(sortedOdd);
            assertArrayEquals(resultOdd, sortedOdd);
        }

        @Test
        @Order(2)
        void sequentialSortedEven() {
            QuickSort.quicksortSequential(sortedEven);
            assertArrayEquals(resultEven, sortedEven);
        }

        @Test
        @Order(3)
        void sequentialUnsortedOdd() {
            QuickSort.quicksortSequential(unsortedOdd);
            assertArrayEquals(resultOdd, unsortedOdd);
        }

        @Test
        @Order(4)
        void sequentialUnsortedEven() {
            QuickSort.quicksortSequential(unsortedEven);
            assertArrayEquals(resultEven, unsortedEven);
        }
        @Test
        @Order(5)
        void sequentialReversedOdd() {
            QuickSort.quicksortSequential(reversedOdd);
            assertArrayEquals(resultOdd, reversedOdd);
        }

        @Test
        @Order(6)
        void sequentialReversedEven() {
            QuickSort.quicksortSequential(reversedEven);
            assertArrayEquals(resultEven, reversedEven);
        }

        @Test
        @Order(7)
        void sequentialExtreme() {
            QuickSort.quicksortSequential(arrayExtreme);
            assertArrayEquals(arrayExtremeSorted, arrayExtreme);
        }
    }

    @Nested
    @DisplayName("Executor")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Executor {

        @Test
        @Order(1)
        void executorSortedOdd() {
            QuickSortExecutor.quicksortExecutor(sortedOdd);
            assertArrayEquals(resultOdd, sortedOdd);
        }

        @Test
        @Order(2)
        void executorSortedEven() {
            QuickSortExecutor.quicksortExecutor(sortedEven);
            assertArrayEquals(resultEven, sortedEven);
        }

        @Test
        @Order(3)
        void executorUnsortedOdd() {
            QuickSortExecutor.quicksortExecutor(unsortedOdd);
            assertArrayEquals(resultOdd, unsortedOdd);
        }

        @Test
        @Order(4)
        void executorUnsortedEven() {
            QuickSortExecutor.quicksortExecutor(unsortedEven);
            assertArrayEquals(resultEven, unsortedEven);
        }

        @Test
        @Order(5)
        void executorReversedOdd() {
            QuickSortExecutor.quicksortExecutor(reversedOdd);
            assertArrayEquals(resultOdd, reversedOdd);
        }

        @Test
        @Order(6)
        void executorReversedEven() {
            QuickSortExecutor.quicksortExecutor(reversedEven);
            assertArrayEquals(resultEven, reversedEven);
        }

        @Test
        @Order(7)
        void executorExtreme() {
            QuickSortExecutor.quicksortExecutor(arrayExtreme);
            assertArrayEquals(arrayExtremeSorted, arrayExtreme);
        }
    }

    @Nested
    @DisplayName("Fork")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Fork {

        @Test
        @Order(1)
        void forkSortedOdd() {
            QuickSortForks.quickSortForks(sortedOdd);
            assertArrayEquals(resultOdd, sortedOdd);
        }

        @Test
        @Order(2)
        void forkSortedEven() {
            QuickSortForks.quickSortForks(sortedEven);
            assertArrayEquals(resultEven, sortedEven);
        }

        @Test
        @Order(3)
        void forkUnsortedOdd() {
            QuickSortForks.quickSortForks(unsortedOdd);
            assertArrayEquals(resultOdd, unsortedOdd);
        }

        @Test
        @Order(4)
        void forkUnsortedEven() {
            QuickSortForks.quickSortForks(unsortedEven);
            assertArrayEquals(resultEven, unsortedEven);
        }

        @Test
        @Order(5)
        void forkReversedOdd() {
            QuickSortForks.quickSortForks(reversedOdd);
            assertArrayEquals(resultOdd, reversedOdd);
        }

        @Test
        @Order(6)
        void forkReversedEven() {
            QuickSortForks.quickSortForks(reversedEven);
            assertArrayEquals(resultEven, reversedEven);
        }

        @Test
        @Order(7)
        void forksExtreme() {
            QuickSortForks.quickSortForks(arrayExtreme);
            assertArrayEquals(arrayExtremeSorted, arrayExtreme);
        }

    }

    @Nested
    @DisplayName("Stream")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Stream {

        @Test
        @Order(1)
        void streamSortedOdd() {
            QuickSortStreams.quicksortStream(sortedOdd);
            assertArrayEquals(resultOdd, sortedOdd);
        }

        @Test
        @Order(2)
        void streamSortedEven() {
            QuickSortStreams.quicksortStream(sortedEven);
            assertArrayEquals(resultEven, sortedEven);
        }

        @Test
        @Order(3)
        void streamUnsortedOdd() {
            QuickSortStreams.quicksortStream(unsortedOdd);
            assertArrayEquals(resultOdd, unsortedOdd);
        }

        @Test
        @Order(4)
        void streamUnsortedEven() {
            QuickSortStreams.quicksortStream(unsortedEven);
            assertArrayEquals(resultEven, unsortedEven);
        }

        @Test
        @Order(5)
        void streamReversedOdd() {
            QuickSortStreams.quicksortStream(reversedOdd);
            assertArrayEquals(resultOdd, reversedOdd);
        }

        @Test
        @Order(6)
        void streamReversedEven() {
            QuickSortStreams.quicksortStream(reversedEven);
            assertArrayEquals(resultEven, reversedEven);
        }

        @Test
        @Order(7)
        void streamExtreme() {
            QuickSortStreams.quicksortStream(arrayExtreme);
            assertArrayEquals(arrayExtremeSorted, arrayExtreme);
        }
    }
}