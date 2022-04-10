package framework.heuristic.inheritance;

import framework.base.SATInstance;

import java.util.Random;

public abstract class InheritanceHeuristic {
    protected final Random rnd;

    public InheritanceHeuristic(Random rnd) {
        this.rnd = rnd;
    }

    protected abstract void applyHeuristic(SATInstance instance, int parentAIndex, int parentBIndex, int childAIndex, int childBIndex);
}
