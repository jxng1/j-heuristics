package framework.heuristic.mutation;

import framework.base.SATInstance;
import framework.base.meme.MemeType;

import java.util.Random;

public abstract class MutationHeuristic {
    protected final Random rnd;

    public MutationHeuristic(Random rnd) {
        this.rnd = rnd;
    }

    public abstract void applyHeuristic(SATInstance instance, int solutionIndex);

    protected double calculateMutationRateOfInstance(SATInstance instance, int solutionIndex) {
        int cleaned = (Integer) instance.getSolutionAtIndex(solutionIndex).getMemeOfType(MemeType.INTENSITY_OF_MUTATION).getValueOfCurrentIndex() % instance.getNumberOfVariables();
        return cleaned / (double) instance.getNumberOfVariables();
    }
}
