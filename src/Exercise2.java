import java.lang.Thread;
import java.util.ArrayList;

public class Exercise2 {
    /**
     *
     * Simple synchronisation
     * 1. Figure out how to use the Java API to extract the number of physical threads available on the CPU you are running on.
     * 2. Write a short program in which n threads for increasing n, say n = 2, 6, 12,
     * increment a shared integer repeatedly,
     * without proper synchronisation, 1,000,000 times,
     * printing the resulting value at the end of the program.
     * Run the program on a multicore system and attempt to exercise the potential race in the program.
     * 3. Modify the program to use "synchronized" to ensure that increments on the shared variable are atomic.
     */
    public static void main(String[] args) throws InterruptedException {
        // Exercise 2.1
        System.out.println("Available physical threads: " + Runtime.getRuntime().availableProcessors());

        // Exercise 2.2
        for (int j = 1; j <= 6; j++) {
            ArrayList<Thread> threads = new ArrayList<>();
            int n = 2 * j;
            int i ;

            MyThread2 instance = new MyThread2();

            for (i = 0; i < n; i++) {
                Thread t = new Thread(instance, String.valueOf(i));
                threads.add(t);
            }

            for (i = 0; i < n; i++) {
                Thread t  = threads.get(i);
                t.start();
            }

            for (i = 0; i < n; i++) {
                threads.get(i).join();
            }

            System.out.println("N: " + n + " = " + instance.counter);
            instance = null;
        }
    }
}


class MyThread2 implements Runnable {

    public int counter = 0;

    public  void run() {
        for (int i = 0; i < 1000000; i++) {
            increment();
        }
    }

    public synchronized void increment( ){
        counter++;
    }
}
