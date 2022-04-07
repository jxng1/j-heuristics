package framework;

import java.util.*;

public class SATInstance {

    //DEBUG
    private final int DEBUG = 1;


    private final int knapsackCapacity;
    private final int numberOfItems;
    private final String instanceName;

    private Map<Integer, Integer> itemMap;
    private Solution[] memory;
    private Solution bestSolution;

    private double objectiveValue;
    private final Random rnd;

    public SATInstance(String instanceName,
                       Random rnd,
                       int populationSize,
                       int numberOfItems,
                       int knapsackCapacity) {
        this.itemMap = new LinkedHashMap<>();
        this.rnd = rnd;
        this.instanceName = instanceName;
        this.numberOfItems = numberOfItems;
        this.knapsackCapacity = knapsackCapacity;
        this.memory = new Solution[populationSize];
    }

    public void addToItemMap(int profit, int weight) {
        itemMap.put(profit, weight);
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getBestSolutionAsBinaryString() {
        return Arrays.toString(bestSolution.getVariables());
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
        if (DEBUG == 1) {
            System.out.println("Length before: " + memory.length);
            System.out.println("New length: " + newSize);
        }

        memory = Arrays.copyOf(memory, Math.min(newSize, memory.length));
    }

    public void generateConstructiveInitialSolution() {
        Map<Integer, Double> map = new HashMap<>();
        itemMap.forEach((k, v) -> {
            map.put(k, (double) k / v);
        });

        var sorted = new ArrayList<>(map.entrySet());
        sorted.sort(Map.Entry.comparingByValue());
        Collections.reverse(sorted);

        int capacity = 0;
        char[] solution = new char[sorted.size()];

        for (var entry : sorted) {
            if (capacity + itemMap.get(entry.getKey()) > knapsackCapacity) {
                solution[sorted.indexOf(entry)] = '0';
            } else {
                solution[sorted.indexOf(entry)] = '1';
                capacity += itemMap.get(entry.getKey());
            }
        }

        this.memory[0] = new Solution(rnd, solution);
    }

    /*
    Swap n bits between solutions A and  B.
     */
    public void swapBitsBetweenSolutions(int indexA, int indexB,
                                         int from) {
        Solution a = memory[indexA];
        Solution b = memory[indexB];

        char[] temp = a.getVariables();

        a.setBits(Arrays.copyOfRange(b.getVariables(), from, a.getVariables().length),
                from);

        b.setBits(temp, from);

        if (DEBUG == 1) {
            System.out.println("A AFTER ONE POINT SWAP: " + Arrays.toString(a.getVariables()));
            System.out.println("B AFTER ONE POINT SWAP: " + Arrays.toString(b.getVariables()));
        }
    }
}
