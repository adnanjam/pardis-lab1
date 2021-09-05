import java.io.*;
import java.lang.Thread;

class Exercise1 {

    public static void main(String[] args) {
        /**
         * Creating and joining threads
         * 1. Write a short program that prints "Hello world" from an additional thread using the Java Thread API.
         * 2. Modify the program to print "Hello world" five times, once from each of five different threads.
         *    Ensure that the strings are not interleaved in the output.
         * 3. Modify the printed string to include the thread number; ensure that all threads have a unique thread number.
         */

        MyThread instance = new MyThread();
        Thread t1 = new Thread(instance, "1");
        Thread t2 = new Thread(instance, "2");
        Thread t3 = new Thread(instance, "3");
        Thread t4 = new Thread(instance, "4");
        Thread t5 = new Thread(instance, "5");

        try {
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();

            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyThread implements Runnable {

    public void run() {
        System.out.println("Hello from thread: " + Thread.currentThread().getName());
    }
}
