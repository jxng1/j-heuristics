package framework.heuristic.inheritance;

import framework.base.SATInstance;

import java.util.Random;

public class SimpleInheritance extends InheritanceHeuristic {
    public SimpleInheritance(Random rnd) {
        super(rnd);
    }

    @Override
    public void applyHeuristic(SATInstance instance, int parentAIndex, int parentBIndex, int childAIndex, int childBIndex) {
        if (instance.getObjectiveFunctionValue(parentAIndex) == instance.getObjectiveFunctionValue(parentBIndex)) {
            int rndIndex = rnd.nextInt(2) == 0 ? parentAIndex : parentBIndex;
            instance.getSolutionAtIndex(childAIndex).setMemes(instance.getSolutionAtIndex(rndIndex).getMemes());
            instance.getSolutionAtIndex(childBIndex).setMemes(instance.getSolutionAtIndex(rndIndex).getMemes());
        }
    }

    @Override
    public String toString() {
        return "Simple Inheritance";
    }
}
