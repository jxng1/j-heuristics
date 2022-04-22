package framework.heuristic.crossover;

import framework.base.SATInstance;

import java.util.Random;

public class OnePointCrossoverHeuristic extends CrossoverHeuristic {
    public OnePointCrossoverHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int parentA, int parentB, int childA, int childB) {
        // get a random point to split
        // not point choosing 0 or the last element, as nothing will get swapped.
        int randomPoint = rnd.nextInt(1, instance.getNumberOfVariables());
        instance.copySolution(parentA, childA);
        instance.copySolution(parentB, childB);

        instance.swapBitsBetweenSolutions(childA, childB, randomPoint);
    }

    @Override
    public String toString() {
        return "One Point Crossover";
    }
}
