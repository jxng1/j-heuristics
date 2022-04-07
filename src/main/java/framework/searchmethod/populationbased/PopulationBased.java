package framework.searchmethod.populationbased;

import framework.SATInstance;

import java.util.Random;

public abstract class PopulationBased {
    protected final int POPULATION_SIZE;
    protected final int BACKUP_SOLUTION_INDEX;

    protected SATInstance instance;
    protected Random rnd = null;

    public PopulationBased(SATInstance instance, Random rnd, int populationSize) {
        this.instance = instance;
        this.rnd = rnd;
        this.POPULATION_SIZE = populationSize;
        this.BACKUP_SOLUTION_INDEX = populationSize * 2 - 1;

        instance.setPopulationSize(populationSize * 2);
    }

    public void run() {

    }
}
