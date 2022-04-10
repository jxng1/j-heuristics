package framework.base;

import framework.TestConfig;
import framework.base.meme.Meme;
import framework.operators.InfeasibleSolutionsOperator;

import java.util.Random;

public class Solution {
    private final SATInstance instance;
    private final Random rnd;
    private boolean[] variables;
    private Meme[] memes;
    private double objectFunctionValue;


    public Solution(SATInstance instance, Random rnd, boolean[] values, Meme[] memes) {
        this.rnd = rnd;
        this.variables = values;
        this.instance = instance;
        this.memes = memes;
    }

    public boolean[] getVariables() {
        return variables;
    }

    public double calculateOFV() {
        var list = instance.getItemList();
        double weight = 0;
        double price = 0;

        for (int i = 0; i < variables.length; i++) {
            if (!variables[i]) continue;

            weight += list.get(i).getWeight();
            price += list.get(i).getPrice();
        }

        if (weight > instance.getKnapsackCapacity()) {
            // repair
            if (TestConfig.getInstance().getInfeasibleSolution() == InfeasibleSolutionsOperator.REPAIR) {
                do {
                    int rndIndex = rnd.nextInt(variables.length);

                    variables[rndIndex] = !variables[rndIndex];

                    if (variables[rndIndex]) {
                        weight += list.get(rndIndex).getWeight();
                        price += list.get(rndIndex).getWeight();
                    } else {
                        weight -= list.get(rndIndex).getWeight();
                        price -= list.get(rndIndex).getWeight();
                    }
                } while (weight > instance.getKnapsackCapacity());
            } else if (TestConfig.getInstance().getInfeasibleSolution() == InfeasibleSolutionsOperator.PENALTY) { // penalty
                return -1;
            }
        }

        return weight + price;
    }

    public void setBits(boolean[] newArr, int from) {
        if (from < 0 || from + newArr.length > variables.length) {
            return; // should error here
        }

        for (int i = from, ite = 0; i < newArr.length; i++, ite++) {
            variables[i] = newArr[ite];
        }
    }
}
