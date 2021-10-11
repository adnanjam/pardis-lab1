import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class LockfreeConcurrentSkipListSetTest {

    @Test
    void uniformDistributionTest() {
        List<Integer> population = Populator.uniform();
        LockfreeConcurrentSkipListSet<Integer> list = new LockfreeConcurrentSkipListSet<>();

        for (int i = 0; i < population.size(); i++) {
            list.add(population.get(i));
        }
    }

    @Test
    void parallelAdd() {
        LockfreeConcurrentSkipListSet<Integer> list = new LockfreeConcurrentSkipListSet<>();
        Random rand = new Random();
        List<Thread> threads = new ArrayList<>();
        List<Integer> vals = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int val = rand.nextInt();
            vals.add(val);
            threads.add(
                new Thread(() -> {
                    list.add(val);
                })
            );
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 10; i++) {
            Assertions.assertTrue(list.contains(vals.get(i)));
        }
    }

    @Test
    void parallelRemove(){
        LockfreeConcurrentSkipListSet<Integer> list = new LockfreeConcurrentSkipListSet<>();
        Random rand = new Random();
        List<Thread> threads = new ArrayList<>();
        int[] vals = new int[]{1,2,3,4,5,6,7,8,9,10};

        for (int i = 0; i < vals.length; i++ ){
            list.add(vals[i]);
        }
        // Remove all values

        for (int i = 0; i < 10; i++) {
            int val = vals[i];

            threads.add(
                    new Thread(() -> {
                        list.remove(val);
                    })
            );
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 10; i++) {
            Assertions.assertFalse(list.contains(vals[i]));
        }
    }

    @Test
    void normalDistributionTest() {

        List<Integer> population = Populator.normal();
        LockfreeConcurrentSkipListSet<Integer> list = new LockfreeConcurrentSkipListSet<>();

        for (int i = 0; i < population.size(); i++) {
            list.add(population.get(i));
        }
    }

    @Test
    void checkDistributionTest() {
        // todo
    }
}
