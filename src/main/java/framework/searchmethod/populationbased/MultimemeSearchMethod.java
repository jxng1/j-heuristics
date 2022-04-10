package framework.searchmethod.populationbased;

import framework.base.SATInstance;
import framework.heuristic.crossover.CrossoverHeuristic;
import framework.heuristic.inheritance.InheritanceHeuristic;
import framework.heuristic.localsearch.LocalSearchHeuristic;
import framework.heuristic.mutation.MutationHeuristic;
import framework.heuristic.replacement.ReplacementHeuristic;
import framework.heuristic.selection.SelectionHeuristic;

import java.util.Random;

public class MultimemeSearchMethod extends PopulationBasedSearchMethod {
    private final double[] innovationRates;

    private final SelectionHeuristic[] selectionHeuristics;
    private final CrossoverHeuristic[] crossoverHeuristics;
    private final MutationHeuristic[] mutationHeuristics;
    private final ReplacementHeuristic[] replacementHeuristics;
    private final InheritanceHeuristic[] inheritanceHeuristics;
    private final LocalSearchHeuristic[] localSearchHeuristics;

    public MultimemeSearchMethod(SATInstance instance,
                                 Random rnd,
                                 int populationSize,
                                 double innovationRates[],
                                 SelectionHeuristic[] selectionHeuristics,
                                 CrossoverHeuristic[] crossoverHeuristics,
                                 MutationHeuristic[] mutationHeuristics,
                                 ReplacementHeuristic[] replacementHeuristics,
                                 InheritanceHeuristic[] inheritanceHeuristics,
                                 LocalSearchHeuristic[] localSearchHeuristics) {
        super(instance, rnd, populationSize);
        this.innovationRates = innovationRates;
        this.selectionHeuristics = selectionHeuristics;
        this.crossoverHeuristics = crossoverHeuristics;
        this.mutationHeuristics = mutationHeuristics;
        this.replacementHeuristics = replacementHeuristics;
        this.inheritanceHeuristics = inheritanceHeuristics;
        this.localSearchHeuristics = localSearchHeuristics;
    }

    @Override
    protected void loop() {

    }
}
