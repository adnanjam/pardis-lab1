import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class Populator {
    static final int POPULATION_SIZE = 100_000;
    static final int MAX_VALUE = 10_000_000;
    static final double variance = 0.1;
    static final double mean = 0.5;

    public static void main(String[] args) {
        List<Integer> population = uniform();
        double n = population.size();

        Double c_mean = 0.0;
        for (int i = 0; i < n; i++) {
            c_mean += (double) population.get(i) / n;
        }

        double c_variance = 0;
        for (int i = 0; i < n; i++) {
            c_variance += Math.pow(c_mean - (double) population.get(i), 2.0) / (double) n;
        }

        System.out.println("N: " + n + " Mean: " + c_mean + " Variance: " + Math.sqrt(c_variance));

    }

    public static List<Integer> normal() {
        List<Integer> population = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add((int) Math.round((rand.nextGaussian() * Math.sqrt(variance) + mean) * MAX_VALUE));
        }

        return population;
    }

    public static List<Integer> uniform() {
        List<Integer> population = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(rand.nextInt(MAX_VALUE));
        }
        return population;
    }
}
