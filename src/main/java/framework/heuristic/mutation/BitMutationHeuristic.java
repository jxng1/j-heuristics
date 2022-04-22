package framework.heuristic.mutation;

import framework.base.SATInstance;

import java.util.Random;

public class BitMutationHeuristic extends MutationHeuristic {
    public BitMutationHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int solutionIndex) {
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

            double rate = calculateMutationRateOfInstance(instance, solutionIndex);
            if (rnd.nextDouble() < rate) {
                instance.bitFlip(i, 1, solutionIndex);
            }
        }

        instance.getSolutionAtIndex(solutionIndex).calculateOFV();
    }

    @Override
    public String toString() {
        return "Bit Mutation";
    }
}
