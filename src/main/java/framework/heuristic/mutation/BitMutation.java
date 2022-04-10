package framework.heuristic.mutation;

import framework.base.SATInstance;

import java.util.Random;

public class BitMutation extends MutationHeuristic {
    public BitMutation(Random rnd, double mutationRate) {
        super(rnd, mutationRate);
    }

    @Override
    protected void applyHeuristic(SATInstance instance, int solutionIndex) {
        for (int i = 0; i < instance.getNumberOfVariables(); i++) {
            if (rnd.nextDouble() < mutationRate) {
                instance.bitFlip(i, solutionIndex);
            }
        }
    }
}
