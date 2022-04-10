package framework.heuristic.localsearch;

import framework.base.SATInstance;

import java.util.Random;

public abstract class LocalSearchHeuristic {
    protected final Random rnd;

    public LocalSearchHeuristic(Random rnd) {
        this.rnd = rnd;
    }

    protected abstract void applyHeuristic(SATInstance instance, int solutionIndex);
}

public enum MoveAcceptance {
    IMPROVEMENT_ONLY, NON_WORSENING, WORSENING, SIMULATED_ANNEALING
}