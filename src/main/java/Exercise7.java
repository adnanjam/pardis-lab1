import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercise7 {
    public static void main(String[] args) {
        LockfreeConcurrentSkipListSet_7<Integer> list = new LockfreeConcurrentSkipListSet_7<>();
        populateList(list, 0);
        list.taskNumber = 4;

        Random r = new Random();
        final int TOTAL_RUNS = 10_000;
        final int THREAD_COUNT = 8;

        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        double p;
        for (int i = 0; i < TOTAL_RUNS; i++) {
            p = r.nextInt(3);
            if (p == 0) {
                service.execute(() -> list.contains(8));
            } else if (p == 1) {
                service.execute(() -> list.remove(8));
            } else {
                service.execute(() -> list.add(8));
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
        for (StringBuilder s : list.globalLogs){
            System.out.print(s.toString());
        }
        System.out.println("================\nEnd of simulation.\n================");
    }

    private static void populateList(LockfreeConcurrentSkipListSet_7 list, int normal) {
        List<Integer> population;

        if (normal == 1) {
            population = Populator.normal();
        } else {
            population = Populator.uniform();
        }

        for (int i = 0; i < population.size(); i++) {
            list.add(population.get(i));
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
