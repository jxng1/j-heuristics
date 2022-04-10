package framework.heuristic.replacement;

import framework.base.SATInstance;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TGAReplacementWithElitismHeuristic extends ReplacementHeuristic {
    public TGAReplacementWithElitismHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    protected int[] generateNewPopulation(SATInstance instance, int populationSize) {
        // choose populationSize amounts of parent/children to form new population
        // get the index of those solutions
        var oldPopulation = instance.getObjectiveFunctionValuesAsList();
        var values = IntStream
                .range(0, populationSize * 2)
                .mapToObj(instance::getObjectiveFunctionValue)
                .sorted()
                .collect(Collectors.toList());
        int[] indices = new int[populationSize];

        int i = 0;
        for (var value : values) {
            indices[i] = oldPopulation.indexOf(value);
            i++;
        }

        return indices;
    }
}
