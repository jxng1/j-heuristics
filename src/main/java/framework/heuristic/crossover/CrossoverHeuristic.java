package framework.heuristic.crossover;

import framework.SATInstance;
import framework.heuristic.Heuristic;

import java.util.Random;

public abstract class CrossoverHeuristic implements Heuristic {
    protected Random rnd;

    public CrossoverHeuristic(Random rnd) {
        this.rnd = rnd;
    }

    public abstract void applyHeuristic(SATInstance instance, int parentA, int parentB);
}
