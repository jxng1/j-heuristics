package framework.heuristic.selection;

import framework.base.SATInstance;

import java.util.Random;

public abstract class SelectionHeuristic {
    protected final Random rnd;

    public SelectionHeuristic(Random rnd) {
        this.rnd = rnd;
    }

    public abstract int applyHeuristic(SATInstance instance, int tournamentSize);
}
