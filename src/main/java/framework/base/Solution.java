package framework.base;

import framework.TestConfig;
import framework.base.meme.Meme;
import framework.base.meme.MemeType;
import framework.operators.InfeasibleSolutionsRefactorOperator;

import java.util.*;
import java.util.stream.Collectors;

public class Solution {
    private final SATInstance instance;
    private final Random rnd;
    private boolean[] variables;
    private Map<MemeType, Meme> currentMemeSelection;
    private double objectFunctionValue;
    private double weight;

    public Solution(SATInstance instance, Random rnd, boolean[] values, Map<MemeType, Meme> availableMemes, boolean mutateBits) {
        this.rnd = rnd;
        this.variables = values;
        this.instance = instance;

        // generate a random meme state selection
        this.currentMemeSelection = new HashMap<>();

        for (Map.Entry<MemeType, Meme> entry : availableMemes.entrySet()) {
            currentMemeSelection.put(entry.getKey(), entry.getValue().deepCopy());
            currentMemeSelection.get(entry.getKey()).setCurrentOption(rnd.nextInt(entry.getValue().getTotalOptions()));
        }

        // add a bit of randomness based on intensity of mutation of the solution
        if (mutateBits) {
            for (int i = 0; i < variables.length; i++) {
                if (rnd.nextDouble() < (Integer) currentMemeSelection.get(MemeType.INTENSITY_OF_MUTATION).getValueOfCurrentIndex() / (double) instance.getNumberOfVariables()) { // can be a config
                    variables[i] = !variables[i];
                }
            }
        }

        calculateOFV();
    }

    private Solution(SATInstance instance, Random rnd, boolean[] values, Map<MemeType, Meme> currentMemeSelection, double objectiveFunctionValue, double weight) {
        this.instance = instance;
        this.rnd = rnd;
        this.variables = values;
        this.currentMemeSelection = currentMemeSelection;
        this.objectFunctionValue = objectiveFunctionValue;
        this.weight = weight;
    }

    public boolean[] getVariables() {
        return variables;
    }

    public double getObjectFunctionValue() {
        return objectFunctionValue;
    }

    public double getWeight() {
        return weight;
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
            if (TestConfig.getInstance().getInfeasibleSolution() == InfeasibleSolutionsRefactorOperator.REPAIR) {
                List<KnapsackItem> originalIncludedItems = new ArrayList<>();
                for (int i = 0; i < variables.length; i++) {
                    if (variables[i]) {
                        originalIncludedItems.add(instance.getItemList().get(i));
                    }
                }

                var sortedIncludedItemsByWeight = originalIncludedItems
                        .stream()
                        .sorted(Comparator.comparing(KnapsackItem::getWeight))
                        .collect(Collectors.toList());

                for (int i = 0; i < sortedIncludedItemsByWeight.size(); i++) {
                    if (weight - sortedIncludedItemsByWeight.get(i).getWeight() <= instance.getKnapsackCapacity()) {
                        variables[originalIncludedItems.indexOf(sortedIncludedItemsByWeight.get(i))] = false;
                        break;
                    }
                }
            } else if (TestConfig.getInstance().getInfeasibleSolution() == InfeasibleSolutionsRefactorOperator.PENALTY) { // penalty
                this.weight = weight;
                this.objectFunctionValue = price / weight;
                return objectFunctionValue;
            }
        }

        this.weight = weight;
        objectFunctionValue = price;
        return objectFunctionValue;
    }

    public Map<MemeType, Meme> getMemes() {
        return currentMemeSelection;
    }

    public void setMemes(Map<MemeType, Meme> memeState) {
        this.currentMemeSelection = memeState;
    }

    public Meme getMemeOfType(MemeType type) {
        return currentMemeSelection.get(type);
    }

    public String getMemesInString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (var entry : currentMemeSelection.entrySet()) {
            stringBuilder.append("[Type: [").append(entry.getKey().toString()).append("] Option: [").append(entry.getValue().getValueOfCurrentIndex().toString()).append("]] ");
        }

        return stringBuilder.toString();
    }

    public void setBits(boolean[] newArr, int from) {
        if (from < 0 || from + newArr.length > variables.length) {
            return; // should error here
        }

        for (int i = from, ite = 0; ite < newArr.length; i++, ite++) {
            variables[i] = newArr[ite];
        }
    }

    public Solution deepCopy() {
        return new Solution(instance, rnd, variables, currentMemeSelection, objectFunctionValue, weight);
    }

    @Override
    public String toString() {
        return "Solution{" +
                "instance=" + instance +
                ", rnd=" + rnd +
                ", variables=" + Arrays.toString(variables) +
                ", currentMemeSelection=" + currentMemeSelection +
                ", objectFunctionValue=" + objectFunctionValue +
                ", weight=" + weight +
                '}';
    }
}
