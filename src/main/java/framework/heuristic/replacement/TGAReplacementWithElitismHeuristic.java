package framework.heuristic.replacement;

import framework.base.SATInstance;
import framework.base.Solution;

import java.util.*;
import java.util.stream.Collectors;

public class TGAReplacementWithElitismHeuristic extends ReplacementHeuristic {
    public TGAReplacementWithElitismHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    protected int[] generateNewPopulation(SATInstance instance, int populationSize) {
        // choose populationSize amounts of parent/children to form new population
        // get the index of those solutions
        var oldPopulation = Arrays.stream(instance.getSolutionsAsArray()).collect(Collectors.toList());
        // needs to be solution
        var sortedSolutionsByOFV = Arrays.stream(instance.getSolutionsAsArray())
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Solution::calculateOFV))
                .collect(Collectors.toList());
        Collections.reverse(sortedSolutionsByOFV);
        int[] indices = new int[populationSize];

        for (int i = 0; i < populationSize; i++) {
            indices[i] = oldPopulation.indexOf(sortedSolutionsByOFV.get(i));
        }

        return indices;
    }

    @Override
    public String toString() {
        return "Trans-generational Replacement With Elitism";
    }
}
