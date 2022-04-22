package framework.heuristic.localsearch;

import framework.base.SATInstance;

import java.util.Random;

public class SteepestHillClimbingHeuristic extends LocalSearchHeuristic {
    public SteepestHillClimbingHeuristic(Random rnd, MoveAcceptance moveAcceptance) {
        super(rnd, moveAcceptance);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int solutionIndex) {
        int index = -1;
        double oldOFV = instance.getObjectiveFunctionValue(solutionIndex);

        for (int i = 0; i < instance.getNumberOfVariables(); i++) {
            // delta evaluation
            if (instance.getSolutionAtIndex(solutionIndex).getVariables()[i]) {
                if (instance.getObjectiveFunctionValue(solutionIndex) - instance.getItemList().get(i).getPrice() < instance.getObjectiveFunctionValue(solutionIndex)
                        || instance.getSolutionAtIndex(solutionIndex).getWeight() - instance.getItemList().get(i).getWeight() <= 0) {
                    continue;
                }
            } else {
                if (instance.getSolutionAtIndex(solutionIndex).getWeight() + instance.getItemList().get(i).getWeight() > instance.getKnapsackCapacity()) {
                    continue;
                }
            }

            instance.bitFlip(i, 1, solutionIndex);
            double candidateScore = instance.getObjectiveFunctionValue(solutionIndex);

            switch (moveAcceptance) {
                case IMPROVEMENT_ONLY -> {
                    if (oldOFV < candidateScore) {
                        index = i;
                        oldOFV = candidateScore;
                        // reason why oldOFV is changed even though solution isn't is because
                        // we can end up skipping a lot of comparisons this way;
                        // when a solution is known to be the best at iteration i,
                        // we can check new neighbours that will be better than that best solution
                    }
                }
                case NON_WORSENING -> {
                    if (oldOFV <= candidateScore) {
                        index = i;
                        oldOFV = candidateScore;
                    }
                }
            }
            instance.bitFlip(i, 1, solutionIndex);
        }

        if (index != -1) {
            instance.bitFlip(index, 1, solutionIndex);
        }
    }

    @Override
    public String toString() {
        return "Steepest Hill Climbing " + moveAcceptance.toString();
    }
}
