package framework.heuristic.localsearch;

import framework.base.SATInstance;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DavisBitHillClimbing extends LocalSearchHeuristic {
    private final MoveAcceptance moveAcceptance;

    public DavisBitHillClimbing(Random rnd, MoveAcceptance moveAcceptance) {
        super(rnd);

        this.moveAcceptance = moveAcceptance;
    }

    @Override
    protected void applyHeuristic(SATInstance instance, int solutionIndex) {
        var permutations = IntStream
                .range(0, instance.getNumberOfVariables())
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(permutations);

        for (Integer permutation : permutations) {
            instance.bitFlip(permutation, solutionIndex);

            double candidateScore = instance.getObjectiveFunctionValue(solutionIndex);
            switch (moveAcceptance) {
                case IMPROVEMENT_ONLY -> {
                    if (candidateScore > instance.getObjectiveFunctionValue(solutionIndex)) {
                        // no need to set as it is already done when calculating it
                    } else {
                        instance.bitFlip(permutation, solutionIndex);
                    }
                }
                case NON_WORSENING -> {
                    if (candidateScore >= instance.getObjectiveFunctionValue(solutionIndex)) {
                        // no need to set as it is already done when calculating it
                    } else {
                        instance.bitFlip(permutation, solutionIndex);
                    }
                }
                case WORSENING -> {
                    // doesn't matter as worsening allowed, so we don't care
                    // maybe it'll work, as randomness seems to work in the world
                }
            }
        }
    }
}
