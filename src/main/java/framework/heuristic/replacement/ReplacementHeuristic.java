package framework.heuristic.replacement;

import framework.base.SATInstance;

import java.util.Random;

public abstract class ReplacementHeuristic {
    protected final Random rnd;

    public ReplacementHeuristic(Random rnd) {
        this.rnd = rnd;
    }

    public void applyHeuristic(SATInstance instance, int populationSize) {
        int[] newPopulation = generateNewPopulation(instance, populationSize);

        // copy solutions of new population to old population
        for (int i = 0; i < populationSize; i++) {
            instance.copySolution(newPopulation[i], i);
        }
    }

    protected abstract int[] generateNewPopulation(SATInstance instance, int populationSize);
}
