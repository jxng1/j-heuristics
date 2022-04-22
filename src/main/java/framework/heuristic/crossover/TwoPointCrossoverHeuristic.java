package framework.heuristic.crossover;

import framework.base.SATInstance;

import java.util.Random;

public class TwoPointCrossoverHeuristic extends CrossoverHeuristic {
    public TwoPointCrossoverHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int parentA, int parentB, int childA, int childB) {
        // not point choosing 0 or the last element, as nothing will get swapped.
        int randomPoint1 = rnd.nextInt(1, instance.getNumberOfVariables());
        int randomPoint2;
        do {
            randomPoint2 = rnd.nextInt(1, instance.getNumberOfVariables());
        } while (randomPoint2 == randomPoint1);
        instance.copySolution(parentA, childA);
        instance.copySolution(parentB, childB);

        instance.swapBitsBetweenSolutions(childA, childB, Math.min(randomPoint1, randomPoint2), Math.max(randomPoint1, randomPoint2));
    }

    @Override
    public String toString() {
        return "Two Point Crossover";
    }
}
