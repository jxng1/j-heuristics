package framework.searchmethod.populationbased;

import framework.SATInstance;

import java.util.Random;

public class Multimeme extends PopulationBased {
    private final double innovationRate;

    public Multimeme(SATInstance instance, Random rnd, int populationSize, double innovationRate) {
        super(instance, rnd, populationSize);
        this.innovationRate = innovationRate;
    }

    @Override
    public void run() {

    }
}
