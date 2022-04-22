package framework.base;

import framework.Runner;
import framework.TestConfig;
import framework.base.meme.Meme;
import framework.base.meme.MemeType;
import framework.operators.InitialSolutionGenerationOperator;

import java.util.*;
import java.util.stream.Collectors;

public class SATInstance {

    private final int knapsackCapacity;
    private final int numberOfItems;
    private final String instanceName;
    private final Random rnd;
    private final Map<MemeType, Meme> memeMap;
    private final List<KnapsackItem> itemList;

    private Solution[] memory;
    private Solution bestSolution;
    private Solution backupBestSolution;
    private int populationSize;
    private long runtime;
    private int generationCount;

    private final List<Pair<Double, Double>> generationalScores;

    public SATInstance(String instanceName,
                       Random rnd,
                       int populationSize,
                       int numberOfItems,
                       int knapsackCapacity,
                       Map<MemeType, Meme> memes) {
        this.itemList = new LinkedList<>();
        this.rnd = rnd;
        this.instanceName = instanceName;
        this.numberOfItems = numberOfItems;
        this.knapsackCapacity = knapsackCapacity;
        this.populationSize = populationSize;
        this.memory = new Solution[populationSize];
        this.memeMap = memes;
        this.runtime = 0;
        this.generationCount = 0;
        this.generationalScores = new LinkedList<Pair<Double, Double>>();
    }

    public List<Pair<Double, Double>> getGenerationalScores() {
        return generationalScores;
    }

    public int getKnapsackCapacity() {
        return knapsackCapacity;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void addToItemList(double profit, double weight) {
        itemList.add(new KnapsackItem(profit, weight));
    }

    public List<KnapsackItem> getItemList() {
        return itemList;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getBestSolutionAsBinaryString() {
        return getSolutionAsBinaryString(backupBestSolution);
    }

    public void bitFlip(int from, int length, int solutionIndex) {
        boolean[] arr = new boolean[length];

        for (int i = 0, ite = from; i < length; i++, ite++) {
            arr[i] = !memory[solutionIndex].getVariables()[ite];
        }

        memory[solutionIndex].setBits(arr, from);
    }

    public void copySolution(int origin, int destination) {
        memory[destination] = memory[origin].deepCopy();
        //System.out.println(memory);
    }

    public Solution getCurrentBestSolution() {
        return bestSolution;
    }

    public int getGenerationsCount() {
        return generationCount;
    }

    public double getObjectiveFunctionValue(Integer index) {
        Solution solution = memory[index];

        double objectiveFunctionValue = solution.calculateOFV();
        if (objectiveFunctionValue > bestSolution.getObjectFunctionValue()) {
            bestSolution = solution;
        }

        if (bestSolution.getObjectFunctionValue() > backupBestSolution.getObjectFunctionValue()) {
            backupBestSolution = bestSolution.deepCopy();
        }

        return objectiveFunctionValue;
    }

    public long getRuntime() {
        return runtime;
    }

    public String getSolutionAsBinaryString(Solution solution) {
        StringBuilder s = new StringBuilder();

        for (boolean b : solution.getVariables()) {
            if (b) {
                s.append(1);
            } else {
                s.append(0);
            }
        }

        return s.toString();
    }

    public String getSolutionAsBinaryString(int index) {
        return getSolutionAsBinaryString(memory[index]);
    }

    public Random getRandom() {
        return rnd;
    }

    public int getNumberOfVariables() {
        return numberOfItems;
    }

    public void setPopulationSize(int newSize) {
        if (Runner.DEBUG == 1) {
            System.out.println("Length before: " + memory.length);
            System.out.println("New length: " + newSize);
        }

        memory = Arrays.copyOf(memory, newSize);
    }

    // TODO: Needs to generate all solutions, choosing a random meme is done by the constructor of the Solution class.
    public void generateInitialSolutions(InitialSolutionGenerationOperator initialSolutionGenerationOperator) {
        switch (initialSolutionGenerationOperator) {
            case GREEDY -> { // generate initial solutions by looking at the price only
                var sorted = itemList
                        .stream()
                        .sorted(Comparator.comparing(KnapsackItem::getPrice))
                        .collect(Collectors.toCollection(LinkedList::new));
                Collections.reverse(sorted);

                int weight = 0;
                boolean[] variables = new boolean[sorted.size()];
                for (KnapsackItem knapsackItem : sorted) {
                    if (weight + knapsackItem.getWeight() < knapsackCapacity) {
                        weight += knapsackItem.getWeight();
                        variables[itemList.indexOf(knapsackItem)] = true;
                    }
                }

                for (int i = 0; i < populationSize; i++) {
                    this.memory[i] = new Solution(this, rnd, variables.clone(), memeMap, true);
                }
            }
            case CONSTRUCTIVE -> { // generate solutions constructively, looking at the price to weight ratio
                var sorted = itemList
                        .stream()
                        .sorted((o1, o2) -> o1.getPriceToWeightRatio() > o2.getPriceToWeightRatio() ? 1 : 0)
                        .collect(Collectors.toCollection(LinkedList::new));

                int weight = 0;
                boolean[] variables = new boolean[sorted.size()];
                for (KnapsackItem knapsackItem : sorted) {
                    if (weight + knapsackItem.getWeight() < knapsackCapacity) {
                        weight += knapsackItem.getWeight();
                        variables[itemList.indexOf(knapsackItem)] = true;
                    }
                }

                for (int i = 0; i < populationSize; i++) {
                    // the first solution is considered 'the best' based on constructiveness
                    if (i == 0) {
                        this.memory[i] = new Solution(this, rnd, variables.clone(), memeMap, false);
                        continue;
                    }
                    // any other solution can be mutated a bit
                    this.memory[i] = new Solution(this, rnd, variables.clone(), memeMap, true);
                }
            }
            case RANDOM -> { // generate solutions randomly
                int weight = 0;
                boolean[] variables = new boolean[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).getWeight() + weight < knapsackCapacity && rnd.nextDouble() < 0.5) {
                        weight += itemList.get(i).getWeight();
                        variables[i] = true;
                    }
                }

                for (int i = 0; i < populationSize; i++) {
                    this.memory[i] = new Solution(this, rnd, variables.clone(), memeMap, true);
                }
            }
        }

        bestSolution = Arrays.stream(memory).min(Comparator.comparing(Solution::calculateOFV)).orElseThrow(NoSuchFieldError::new);
        if (backupBestSolution == null || backupBestSolution.getObjectFunctionValue() < bestSolution.getObjectFunctionValue()) {
            backupBestSolution = bestSolution.deepCopy();
        }
    }

    public Solution getSolutionAtIndex(int index) {
        return memory[index];
    }

    public Solution[] getSolutionsAsArray() {
        return memory;
    }

    /*
    Swap n bits between solutions A and B from a given index.
    Note that the index to swap from is inclusive e.g. given an array of [true, false, false, false, true], if the index is 2 then the array to swap would be [false, false, true].
     */
    public void swapBitsBetweenSolutions(int indexA, int indexB,
                                         int from) {
        swapBitsBetweenSolutions(indexA, indexB, from, getNumberOfVariables());
    }

    /*
    Swap n bits between solutions A and B from a given index to a given index.
    Note that the index to swap from is inclusive e.g. given an array of [true, false, false, false, true], if the index is 2 then the array to swap would be [false, false, true].
     */
    public void swapBitsBetweenSolutions(int indexA, int indexB,
                                         int from, int to) {
        Solution a = memory[indexA];
        Solution b = memory[indexB];

        var aSwap = Arrays.copyOfRange(b.getVariables(), from, to);
        var bSwap = Arrays.copyOfRange(a.getVariables(), from, to);
        a.setBits(aSwap, from);
        b.setBits(bSwap, from);

        if (Runner.DEBUG == 1) {
            System.out.println("A AFTER SWAP: " + Arrays.toString(a.getVariables()));
            System.out.println("B AFTER SWAP: " + Arrays.toString(b.getVariables()));
        }
    }

    // Update the status of the instance e.g. update solutions, so they know they've just calculated OFV.
    public void update(long t0) {
        runtime = System.currentTimeMillis() - t0;
        var sortedSolutionsByOFV = Arrays.stream(memory)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Solution::calculateOFV))
                .collect(Collectors.toList());

        if (generationCount < TestConfig.getInstance().getGenerationOutputCount() - 1) {
            generationalScores.add(new Pair<>(sortedSolutionsByOFV.get(0).getObjectFunctionValue(),
                    sortedSolutionsByOFV.get(sortedSolutionsByOFV.size() - 1).getObjectFunctionValue()));
        } else if (generationCount == TestConfig.getInstance().getGenerationOutputCount() - 1) {
            generationalScores.add(new Pair<>(sortedSolutionsByOFV.get(0).getObjectFunctionValue(),
                    bestSolution.getObjectFunctionValue()));
        }

        generationCount++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SATInstance instance = (SATInstance) o;
        return knapsackCapacity == instance.knapsackCapacity && numberOfItems == instance.numberOfItems && populationSize == instance.populationSize && runtime == instance.runtime && generationCount == instance.generationCount && Objects.equals(instanceName, instance.instanceName) && Objects.equals(rnd, instance.rnd) && Objects.equals(memeMap, instance.memeMap) && Objects.equals(itemList, instance.itemList) && Arrays.equals(memory, instance.memory) && Objects.equals(bestSolution, instance.bestSolution);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(knapsackCapacity, numberOfItems, instanceName, rnd, memeMap, itemList, bestSolution, populationSize, runtime, generationCount);
        result = 31 * result + Arrays.hashCode(memory);
        return result;
    }
}
