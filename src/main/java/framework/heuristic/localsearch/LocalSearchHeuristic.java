package framework.heuristic.localsearch;

import framework.base.SATInstance;

import java.util.Random;

public abstract class LocalSearchHeuristic {
    protected final Random rnd;
    protected final MoveAcceptance moveAcceptance;

    public LocalSearchHeuristic(Random rnd, MoveAcceptance moveAcceptance) {
        this.rnd = rnd;
        this.moveAcceptance = moveAcceptance;
    }

    public abstract void applyHeuristic(SATInstance instance, int solutionIndex);
}

