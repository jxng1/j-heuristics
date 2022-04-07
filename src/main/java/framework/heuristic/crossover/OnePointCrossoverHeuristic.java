package framework.heuristic.crossover;

import framework.SATInstance;

import java.util.Random;

public class OnePointCrossoverHeuristic extends CrossoverHeuristic {
    public OnePointCrossoverHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int parentA, int parentB) {
        // get a random point to split
        int randomPoint = rnd.nextInt(instance.getNumberOfVariables());

        instance.swapBitsBetweenSolutions(parentA, parentB, randomPoint);
    }
}
