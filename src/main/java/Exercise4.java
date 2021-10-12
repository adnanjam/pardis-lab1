import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercise4 {
    public static void main(String[] args) {
        LockfreeConcurrentSkipListSet<Integer> list = new LockfreeConcurrentSkipListSet<>();
        Exercise3.populateList(list, 0);
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
        System.out.println("================\nEnd of simulation.\n================");
    }
}
