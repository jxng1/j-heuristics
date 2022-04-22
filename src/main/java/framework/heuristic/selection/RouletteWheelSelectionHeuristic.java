package framework.heuristic.selection;

import framework.base.SATInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RouletteWheelSelectionHeuristic extends SelectionHeuristic {
    public RouletteWheelSelectionHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public int applyHeuristic(SATInstance instance, int tournamentSize) {
        // calculate section probabilities
        Map<Integer, Double> wheel = new HashMap<>();
        for (int i = 0; i < tournamentSize; i++) {
            wheel.put(i, calculateRouletteProbabilityOfSolution(instance, i, tournamentSize));
        }

        // spin the wheel
        int ret = -1;
        double summedProbability = wheel.values().stream().mapToDouble(v -> v).sum();
        double p = 0;
        do {
            double val = rnd.nextDouble(0, summedProbability);
            p += val;
            ret = (ret + 1) % tournamentSize;
        } while (p < summedProbability);

        return ret;
    }

    @Override
    public String toString() {
        return "Roulette Wheel Selection";
    }

    private double calculateRouletteProbabilityOfSolution(SATInstance instance, int solutionIndex, int tournamentSize) {
        return instance.getSolutionAtIndex(solutionIndex).getObjectFunctionValue() / tournamentSize;
    }
}
