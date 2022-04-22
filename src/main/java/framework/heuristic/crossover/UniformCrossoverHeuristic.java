package framework.heuristic.crossover;

import framework.base.SATInstance;

import java.util.Random;

public class UniformCrossoverHeuristic extends CrossoverHeuristic {
    public UniformCrossoverHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int parentA, int parentB, int childA, int childB) {
        instance.copySolution(parentA, childA);
        instance.copySolution(parentB, childB);
        for (int i = 0; i < instance.getNumberOfVariables(); i++) {
            if (rnd.nextDouble() < 0.5) { // coin toss on the crossover
                instance.swapBitsBetweenSolutions(childA, childB, i, i + 1);
            }
        }
    }

    @Override
    public String toString() {
        return "Uniform Crossover";
    }
}
