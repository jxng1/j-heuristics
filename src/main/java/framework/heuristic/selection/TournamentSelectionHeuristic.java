package framework.heuristic.selection;

import framework.base.SATInstance;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TournamentSelectionHeuristic extends SelectionHeuristic {
    public TournamentSelectionHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public int applyHeuristic(SATInstance instance, int tournamentSize) {
        double best = Double.NEGATIVE_INFINITY;
        var rndListOfIndexes =
                IntStream.range(0, instance.getPopulationSize() - 1)
                        .boxed()
                        .collect(Collectors.toList());
        Collections.shuffle(rndListOfIndexes, rnd);
        int ret = -1;

        for (int i = 0; i < tournamentSize - 1; i++) {
            double temp = instance.getObjectiveFunctionValue(rndListOfIndexes.get(i));
            if (temp > best) {
                best = temp;
                ret = i;
            }
        }

        return ret;
    }

    @Override
    public String toString() {
        return "Tournament Selection";
    }
}
