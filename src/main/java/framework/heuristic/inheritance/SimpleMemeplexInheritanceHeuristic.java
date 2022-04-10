package framework.heuristic.inheritance;

import framework.base.SATInstance;

import java.util.Random;

public class SimpleMemeplexInheritanceHeuristic extends InheritanceHeuristic {
    public SimpleMemeplexInheritanceHeuristic(Random rnd) {
        super(rnd);
    }

    @Override
    protected void applyHeuristic(SATInstance instance, int parentAIndex, int parentBIndex, int childAIndex, int childBIndex) {
        if (instance.getObjectiveFunctionValue(parentAIndex) == instance.getObjectiveFunctionValue(parentBIndex)) {
            int rndIndex = rnd.nextInt(2) == 0 ? parentAIndex : parentBIndex;
            instance.getSolutionAtIndex(childAIndex).setMemes(instance.getSolutionAtIndex(rndIndex).getMemes());
            instance.getSolutionAtIndex(childBIndex).setMemes(instance.getSolutionAtIndex(rndIndex).getMemes());
        }
    }
}
