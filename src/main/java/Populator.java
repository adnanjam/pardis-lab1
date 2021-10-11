import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Populator {
    static final int POPULATION_SIZE = 10_000;
    static final int MAX_VALUE = 10_000_000;
    static final double variance = 0.1;
    static final double mean = 0.5;

    public static void main(String[] args){
        Random rand = new Random();

        System.out.println();
    }

    public static List<Integer> normal(){
        List<Integer> population = new ArrayList<>();
        Random rand = new Random();

        for(int i = 0; i < POPULATION_SIZE; i++){
            population.add((int) Math.round((rand.nextGaussian() + mean * variance)  * MAX_VALUE));
        }

        return  population;
    }

    public static List<Integer> uniform(){
        List<Integer> population = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0; i < POPULATION_SIZE; i++){
            population.add(rand.nextInt(MAX_VALUE));
        }
        return population;
    }
}
