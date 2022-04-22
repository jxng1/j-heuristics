package framework.heuristic.mutation;

import framework.base.SATInstance;

import java.util.Arrays;
import java.util.Random;

public class InversionMutationHeuristic extends MutationHeuristic {
    public InversionMutationHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int solutionIndex) {
        int randomPointA = rnd.nextInt(0, instance.getNumberOfVariables());
        int randomPointB = rnd.nextInt(0, instance.getNumberOfVariables());

        if (instance.getSolutionAtIndex(solutionIndex).getVariables()[randomPointA] == instance.getSolutionAtIndex(solutionIndex).getVariables()[randomPointB]) {
            return; // no point of the swap as they are the same e.g. both true/false
        } else {
            int min = Math.min(randomPointA, randomPointB);
            int max = Math.max(randomPointA, randomPointB);

            for (int i = max, check = min; i >= min; i--) {
                if (instance.getSolutionAtIndex(solutionIndex).getVariables()[i]) { // if variables[i] is true
                    // check the weight + the sum of the item swapped is feasible
                    if (instance.getSolutionAtIndex(solutionIndex).getWeight() + instance.getItemList().get(check).getWeight() - instance.getItemList().get(i).getWeight() > instance.getKnapsackCapacity()) {
                        return;
                    }
                } else {
                    // check the weight + the sum of the item swapped is feasible
                    if (instance.getSolutionAtIndex(solutionIndex).getWeight() - instance.getItemList().get(check).getWeight() + instance.getItemList().get(i).getWeight() > instance.getKnapsackCapacity()) {
                        return;
                    }
                }
            }
        }

        double rate = calculateMutationRateOfInstance(instance, solutionIndex);
        if (rnd.nextDouble() < rate) {
            boolean[] temp = Arrays.copyOfRange(instance.getSolutionAtIndex(solutionIndex).getVariables(), Math.min(randomPointA, randomPointB), Math.max(randomPointA, randomPointB) + 1);

            for (int i = temp.length - 1, ite = Math.min(randomPointA, randomPointB); i >= 0; i--, ite++) {
                instance.getSolutionAtIndex(solutionIndex).setBits(new boolean[]{temp[i]}, ite);
            }
            instance.getSolutionAtIndex(solutionIndex).calculateOFV();
        }
    }

    @Override
    public String toString() {
        return "Inversion Mutation";
    }
}
