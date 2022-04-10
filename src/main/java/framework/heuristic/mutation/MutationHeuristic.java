package framework.heuristic.mutation;

import framework.base.SATInstance;

import java.util.Random;

public abstract class MutationHeuristic {
    protected final Random rnd;
    protected final double mutationRate;

    public MutationHeuristic(Random rnd, double mutationRate) {
        this.rnd = rnd;
        this.mutationRate = mutationRate;
    }

    protected abstract void applyHeuristic(SATInstance instance, int index);
}
