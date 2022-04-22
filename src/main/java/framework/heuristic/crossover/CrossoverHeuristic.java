package framework.heuristic.crossover;

import framework.base.SATInstance;

import java.util.Random;

public abstract class CrossoverHeuristic {
    protected final Random rnd;

    public CrossoverHeuristic(Random rnd) {
        this.rnd = rnd;
    }

    public abstract void applyHeuristic(SATInstance instance, int parentA, int parentB, int childA, int childB);
}
