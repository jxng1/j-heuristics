package framework.heuristic.mutation;

import framework.base.SATInstance;

import java.util.Random;

public class SwapMutationHeuristic extends MutationHeuristic {
    public SwapMutationHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int solutionIndex) {
        int randomPointA = rnd.nextInt(0, instance.getNumberOfVariables());
        int randomPointB = rnd.nextInt(0, instance.getNumberOfVariables());

        if (instance.getSolutionAtIndex(solutionIndex).getVariables()[randomPointA] == instance.getSolutionAtIndex(solutionIndex).getVariables()[randomPointB]) {
            return; // no point of the swap as they are the same e.g. both true/false
        } else {
            // either a is true and b is false
            if (instance.getSolutionAtIndex(solutionIndex).getVariables()[randomPointA] && !instance.getSolutionAtIndex(solutionIndex).getVariables()[randomPointB]) {
                // - at randomPointA + at randomPointB
                // if new weight exceeds or new objective function value is lower than previous
                if (instance.getSolutionAtIndex(solutionIndex).getWeight() - instance.getItemList().get(randomPointA).getWeight() + instance.getItemList().get(randomPointB).getWeight() > instance.getSolutionAtIndex(solutionIndex).getWeight()
                        || instance.getSolutionAtIndex(solutionIndex).getObjectFunctionValue() - instance.getItemList().get(randomPointA).getPrice() + instance.getItemList().get(randomPointB).getPrice() < instance.getSolutionAtIndex(solutionIndex).getWeight()) {
                    return;
                }
            } else { // or otherwise
                // + randomPointA - randomPointBs
                // if new weight exceeds or new objective function value is lower than previous
                if (instance.getSolutionAtIndex(solutionIndex).getWeight() + instance.getItemList().get(randomPointA).getWeight() - instance.getItemList().get(randomPointB).getWeight() > instance.getSolutionAtIndex(solutionIndex).getWeight()
                        || instance.getSolutionAtIndex(solutionIndex).getObjectFunctionValue() + instance.getItemList().get(randomPointA).getPrice() - instance.getItemList().get(randomPointB).getPrice() < instance.getSolutionAtIndex(solutionIndex).getWeight()) {
                    return;
                }
            }
        }

        double rate = calculateMutationRateOfInstance(instance, solutionIndex);
        if (rnd.nextDouble() < rate) {
            boolean temp = instance.getSolutionAtIndex(solutionIndex).getVariables()[randomPointA];
            instance.getSolutionAtIndex(solutionIndex).setBits(new boolean[]{instance.getSolutionAtIndex(solutionIndex).getVariables()[randomPointB]}, randomPointA);
            instance.getSolutionAtIndex(solutionIndex).setBits(new boolean[]{temp}, randomPointB);
            instance.getSolutionAtIndex(solutionIndex).calculateOFV();
        }
    }

    @Override
    public String toString() {
        return "Swap Mutation";
    }
}
