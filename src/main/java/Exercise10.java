import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Exercise10 {
    final static int THREAD_COUNT = 6;
    final static int TOTAL_RUNS = 10_000;

    private static class Aggregator implements Runnable {

        HashMap<String, StringBuilder> logs;
        public ArrayBlockingQueue<String[]> queue = new ArrayBlockingQueue<String[]>(TOTAL_RUNS);

        public Aggregator() {
            logs = new HashMap<String, StringBuilder>(THREAD_COUNT); // each thread will have a list for the logs
        }

        @Override
        public void run() {
            while (!queue.isEmpty()) {
                try {
                    String[] log = queue.take();
                    String threadId = log[0];
                    String payload = log[1];
                    String value = log[2];

                    // Initialise for this thread if there is no StringBuilder
                    if(!logs.containsKey(threadId)){
                        logs.put(threadId, new StringBuilder());
                    }

                    logs.get(threadId).append(payload + " , " + value + " \n");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            System.out.println("Aggregator exiting");
        }

    }

    public static void main(String[] args) {


        Aggregator agg = new Aggregator();

        Thread t = new Thread(agg);

        LockfreeConcurrentSkipListSet_10<Integer> list = new LockfreeConcurrentSkipListSet_10<>(agg.queue);
        populateList(list, 0);

        list.taskNumber = 4;

        Random r = new Random();

        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT - 1); // reserve one thread for Agg.
        double p;

        service.execute(agg);

        for (int i = 0; i < TOTAL_RUNS; i++) {
            p = r.nextInt(3);

            if (p == 0) {
                service.execute(() -> {
                    try {
                        list.contains(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } else if (p == 1) {
                service.execute(() -> {
                    try {
                        list.remove(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                service.execute(() -> {
                    try {
                        list.add(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }


            if (i == TOTAL_RUNS / 10) {
                t.start();
            }
        }

        service.shutdown();

        while (true) {
            try {
                if (service.awaitTermination(500, TimeUnit.MICROSECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String key : agg.logs.keySet()) {
            System.out.print(agg.logs.get(key).toString());
        }

        System.out.println("================\nEnd of simulation.\n================");
    }

    private static void populateList(LockfreeConcurrentSkipListSet_10 list, int normal) {
        List<Integer> population;

        if (normal == 1) {
            population = Populator.normal();
        } else {
            population = Populator.uniform();
        }

        for (int i = 0; i < population.size(); i++) {
            try {
                list.add(population.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static long arraySum(List<Long> array) {
        long sum = 0;
        for (int i = 0; i < array.size(); i++) {
            sum += array.get(i);
        }

        return sum;
    }


}
