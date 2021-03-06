package framework.searchmethod.populationbased;

import framework.base.SATInstance;

import java.util.Random;

public abstract class PopulationBasedSearchMethod {
    protected final int POPULATION_SIZE;
    protected final int BACKUP_SOLUTION_INDEX;
    protected final int TOURNAMENT_SIZE;

    protected SATInstance instance;
    protected final Random rnd;

    public PopulationBasedSearchMethod(SATInstance instance, Random rnd, int populationSize, int tournamentSize) {
        this.instance = instance;
        this.rnd = rnd;
        this.POPULATION_SIZE = populationSize;
        this.TOURNAMENT_SIZE = tournamentSize;
        this.BACKUP_SOLUTION_INDEX = populationSize * 2 - 1;

        instance.setPopulationSize(populationSize * 2);
    }

    public void run() {
        loop();
    }

    protected abstract void loop();
}
