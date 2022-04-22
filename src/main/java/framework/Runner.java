package framework;

import framework.base.SATInstance;
import framework.searchmethod.populationbased.MultimemeSearchMethod;
import util.FileUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Runner {

    public static final int DEBUG = 3;

    public static List<File> INSTANCES_FILES;

    public static void main(String[] args) {
        INSTANCES_FILES = FileUtil.getInstance().getInputAsList();
        TestConfig config = TestConfig.getInstance();

        // TODO: THIS
        Map<String, List<SATInstance>> resultsMap = INSTANCES_FILES.stream().collect(Collectors.toMap(File::getName,
                file -> IntStream.range(0, config.getTrialsCount()).parallel().mapToObj(run -> {
                    SATInstance instance = FileUtil
                            .getInstance()
                            .readInstanceFromFile(file, System.currentTimeMillis());

                    MultimemeSearchMethod multimemeSearchMethod = new MultimemeSearchMethod(instance,
                            instance.getRandom(),
                            config.getPopulationSize(),
                            config.getTournamentSize(),
                            config.getInnovationRate(),
                            config.getSelectionOperator()
                    );

                    long t0 = System.currentTimeMillis();
                    while (instance.getRuntime() <= config.getRuntime() &&
                            !(instance.getGenerationsCount() == config.getMaxGenerations())) {
                        multimemeSearchMethod.run();

                        if (DEBUG == 2) {
                            System.out.println("===GENERATION " + (instance.getGenerationsCount() - 1) + "===");
                            for (int z = 0; z < instance.getPopulationSize(); z++) {
                                System.out.println("=Solution Index: [" + z + "]=");
                                System.out.println("Solution String: [" + instance.getSolutionAsBinaryString(z) + "]");
                                System.out.println("Solution OFV: [" + instance.getSolutionAtIndex(z).getObjectFunctionValue() + "]");
                                System.out.println("=Solution Index: [" + z + "]=");
                            }
                            // update
                            System.out.println("Current Best Score: " + instance.getCurrentBestSolution().getObjectFunctionValue());
                            System.out.println("===GENERATION " + (instance.getGenerationsCount() - 1) + "===");
                        }

                        instance.update(t0);
                    }
                    return instance;
                }).collect(Collectors.toCollection(LinkedList::new))));

        if (DEBUG == 2 || DEBUG == 3) {
            for (Map.Entry<String, List<SATInstance>> entry : resultsMap.entrySet()) {
                System.out.println("===INSTANCE NAME: " + entry.getKey());
                for (SATInstance trial : entry.getValue()) {
                    int trialNumber = entry.getValue().indexOf(trial) + 1;
                    System.out.println("===TRIAL [" + trialNumber + "]===");
                    System.out.println("Final Solution String: [" + trial.getBestSolutionAsBinaryString() + "]");
                    System.out.println("Best Solution OFV: [" + trial.getCurrentBestSolution().getObjectFunctionValue() + "]");
                    System.out.println("Best Solution Memeplex: [" + trial.getCurrentBestSolution().getMemesInString());
                    System.out.println("Final Generation Count: [" + trial.getGenerationsCount() + "]");
                    System.out.println("Runtime: [" + trial.getRuntime() + "ms]");
                    try {
                        FileUtil.getInstance().writeTestInstanceToFile(trial, trialNumber);
                    } catch (Exception e) {
                        System.out.println("[WARNING] Error occurred whilst saving to output file.");
                    }
                }
            }
        }
    }
}