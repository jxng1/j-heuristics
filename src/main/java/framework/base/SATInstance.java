package framework.base;

import framework.Runner;
import framework.base.meme.Meme;
import framework.base.meme.MemeType;

import java.util.*;
import java.util.stream.Collectors;

public class SATInstance {

    private final int knapsackCapacity;
    private final int numberOfItems;
    private final String instanceName;
    private final Random rnd;

    private final List<KnapsackItem> itemList;
    private Solution[] memory;
    private Solution bestSolution;
    private int populationSize;
    private double bestObjectiveFunctionValue = Double.POSITIVE_INFINITY;

    public SATInstance(String instanceName,
                       Random rnd,
                       int populationSize,
                       int numberOfItems,
                       int knapsackCapacity,
                       Map<MemeType, Meme[]> memes,
                       InitialSolutionGeneration initialSolutionGeneration) {
        this.itemList = new LinkedList<>();
        this.rnd = rnd;
        this.instanceName = instanceName;
        this.numberOfItems = numberOfItems;
        this.knapsackCapacity = knapsackCapacity;
        this.populationSize = populationSize;
        this.memory = new Solution[populationSize];

        generateConstructiveInitialSolution(initialSolutionGeneration, memes);
    }

    public int getKnapsackCapacity() {
        return knapsackCapacity;
    }

    public double getBestObjectiveValue() {
        return bestObjectiveFunctionValue;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void addToItemList(int profit, int weight) {
        itemList.add(new KnapsackItem(profit, weight));
    }

    public List<KnapsackItem> getItemList() {
        return itemList;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getBestSolutionAsBinaryString() {
        return Arrays.toString(bestSolution.getVariables());
    }

    public void bitFlip(int i, int solutionIndex) {
        memory[i].setBits(new boolean[]{!memory[i].getVariables()[i]}, i);
    }

    public void copySolution(int origin, int destination) {
        memory[destination] = memory[origin];
    }

    public double getObjectiveFunctionValue(Integer index) {
        Solution solution = memory[index];

        double objectiveFunctionValue = solution.calculateOFV();

        if (objectiveFunctionValue < bestObjectiveFunctionValue) {
            bestSolution = solution;
            bestObjectiveFunctionValue = objectiveFunctionValue;
        }

        return objectiveFunctionValue;
    }

    public List<Double> getObjectiveFunctionValuesAsList() {
        return Arrays.stream(memory).mapToDouble(Solution::calculateOFV).boxed().collect(Collectors.toCollection(LinkedList::new));
    }

    public String getSolutionAsBinaryString(Solution solution) {
        return Arrays.toString(solution.getVariables());
    }

    public String getSolutionAsBinaryString(int index) {
        return Arrays.toString(memory[index].getVariables());
    }

    public Random getRandom() {
        return rnd;
    }

    public int getNumberOfVariables() {
        return bestSolution.getVariables().length;
    }

    public void setPopulationSize(int newSize) {
        if (Runner.DEBUG == 1) {
            System.out.println("Length before: " + memory.length);
            System.out.println("New length: " + newSize);
        }

        memory = Arrays.copyOf(memory, Math.min(newSize, memory.length));
    }

    public void generateConstructiveInitialSolution(InitialSolutionGeneration initialSolutionGeneration, Meme[] memes) {
        var sorted = new ArrayList<>(itemList);
        sorted = sorted
                .stream()
                .sorted((o1, o2) -> o1.getPriceToWeightRatio() > o2.getPriceToWeightRatio() ? 1 : 0)
                .collect(Collectors.toCollection(ArrayList::new));

        int capacity = 0;
        boolean[] solution = new boolean[sorted.size()];

        for (var entry : sorted) {
            if (capacity + entry.getWeight() > knapsackCapacity) {
                solution[sorted.indexOf(entry)] = false;
            } else {
                solution[sorted.indexOf(entry)] = true;
                capacity += entry.getWeight();
            }
        }

        this.memory[0] = new Solution(this, rnd, solution, memes);
    }

    public Solution getSolutionAtIndex(int index) {
        return memory[index];
    }

    /*
    Swap n bits between solutions A and  B.
     */
    public void swapBitsBetweenSolutions(int indexA, int indexB,
                                         int from) {
        Solution a = memory[indexA];
        Solution b = memory[indexB];

        boolean[] temp = a.getVariables();

        a.setBits(Arrays.copyOfRange(b.getVariables(), from, a.getVariables().length),
                from);

        b.setBits(temp, from);

        if (Runner.DEBUG == 1) {
            System.out.println("A AFTER ONE POINT SWAP: " + Arrays.toString(a.getVariables()));
            System.out.println("B AFTER ONE POINT SWAP: " + Arrays.toString(b.getVariables()));
        }
    }
}
