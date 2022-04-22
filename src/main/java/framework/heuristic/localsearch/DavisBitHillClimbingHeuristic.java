package framework.heuristic.localsearch;

import framework.base.SATInstance;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DavisBitHillClimbingHeuristic extends LocalSearchHeuristic {
    public DavisBitHillClimbingHeuristic(Random rnd, MoveAcceptance moveAcceptance) {
        super(rnd, moveAcceptance);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int solutionIndex) {
        var permutations = IntStream
                .range(0, instance.getNumberOfVariables())
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(permutations);

        for (Integer permutation : permutations) {
            // delta evaluation; no point flipping and continuing as it's going to go over the limit anyways
            if (instance.getSolutionAtIndex(solutionIndex).getVariables()[permutation]) {
                if (instance.getObjectiveFunctionValue(solutionIndex) - instance.getItemList().get(permutation).getPrice() < instance.getObjectiveFunctionValue(solutionIndex)
                        || instance.getSolutionAtIndex(solutionIndex).getWeight() - instance.getItemList().get(permutation).getWeight() <= 0) {
                    continue;
                }
            } else {
                if (instance.getSolutionAtIndex(solutionIndex).getWeight() + instance.getItemList().get(permutation).getWeight() > instance.getKnapsackCapacity()) {
                    continue;
                }
            }

            double oldOFV = instance.getObjectiveFunctionValue(solutionIndex);
            instance.bitFlip(permutation, 1, solutionIndex);
            double candidateScore = instance.getObjectiveFunctionValue(solutionIndex);

            switch (moveAcceptance) {
                case IMPROVEMENT_ONLY -> {
                    if (candidateScore > oldOFV) {
                        // no need to set as it is already done when calculating it
                    } else {
                        instance.bitFlip(permutation, 1, solutionIndex);
                    }
                }
                case NON_WORSENING -> {
                    if (candidateScore >= oldOFV) {
                        // no need to set as it is already done when calculating it
                    } else {
                        instance.bitFlip(permutation, 1, solutionIndex);
                    }
                }
                case WORSENING -> {
                    // doesn't matter as worsening allowed, so we don't care
                    // maybe it'll work, as randomness seems to work in the world
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Davis Bit Hill Climbing " + moveAcceptance.toString();
    }
}
