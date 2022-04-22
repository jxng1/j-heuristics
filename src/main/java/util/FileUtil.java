package util;

import framework.TestConfig;
import framework.base.Pair;
import framework.base.SATInstance;
import framework.base.meme.Meme;
import framework.base.meme.MemeType;
import framework.heuristic.crossover.CrossoverHeuristic;
import framework.heuristic.crossover.OnePointCrossoverHeuristic;
import framework.heuristic.crossover.TwoPointCrossoverHeuristic;
import framework.heuristic.crossover.UniformCrossoverHeuristic;
import framework.heuristic.inheritance.InheritanceHeuristic;
import framework.heuristic.inheritance.SimpleInheritance;
import framework.heuristic.localsearch.DavisBitHillClimbingHeuristic;
import framework.heuristic.localsearch.LocalSearchHeuristic;
import framework.heuristic.localsearch.MoveAcceptance;
import framework.heuristic.localsearch.SteepestHillClimbingHeuristic;
import framework.heuristic.mutation.BitMutationHeuristic;
import framework.heuristic.mutation.InversionMutationHeuristic;
import framework.heuristic.mutation.MutationHeuristic;
import framework.heuristic.mutation.SwapMutationHeuristic;
import framework.heuristic.replacement.ReplacementHeuristic;
import framework.heuristic.replacement.TGAReplacementWithElitismHeuristic;
import framework.heuristic.selection.RouletteWheelSelectionHeuristic;
import framework.heuristic.selection.SelectionHeuristic;
import framework.heuristic.selection.TournamentSelectionHeuristic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

public class FileUtil {
    private static FileUtil instance;

    private FileUtil() {

    }

    public static FileUtil getInstance() {
        if (Objects.equals(instance, null)) {
            instance = new FileUtil();
        }

        return instance;
    }

    public List<File> getInputAsList() {
        List<SATInstance> ret = new ArrayList<>();

        File directory = new File(Objects.requireNonNull(getClass().getResource("/instances")).getPath().replace("%20", " "));
        return Arrays.stream(Objects.requireNonNull(directory.listFiles())).toList();
    }

    public SATInstance readInstanceFromFile(File file, long seed) {
        try {
            Scanner fileScanner = new Scanner(file);

            var firstLineArr = Arrays
                    .stream(fileScanner.nextLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            Random rnd = new Random(seed);

            SATInstance ret = new SATInstance(file.getName()
                    .replace(".txt", ""),
                    rnd,
                    TestConfig.getInstance().getPopulationSize(),
                    firstLineArr[0], firstLineArr[1],
                    createMemeMapOfInstance(rnd, TestConfig.getInstance()));

            while (fileScanner.hasNextLine()) {
                double[] line = Arrays
                        .stream(fileScanner.nextLine().split(" "))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                ret.addToItemList(line[0], line[1]);
            }

            ret.generateInitialSolutions(TestConfig.getInstance().getInitialSolutionGeneration());
            fileScanner.close();
            return ret;
        } catch (FileNotFoundException e) {
            System.err.println("[WARNING] Error occurred whilst reading instance from file." + e);
        }

        return null;
    }

    public void writeTestInstanceToFile(SATInstance instance, int trial) throws Exception {
        File directory = new File((getClass().getResource("/output")).getPath().replace("%20", " "));
        //System.out.println(directory.getPath());
        if (!directory.exists() || directory.isFile()) {
            boolean success = directory.mkdir();
            System.out.println("[INFO] Created new directory for output, success: [" + success + "].");

            if (!success) {
                throw new Exception("[WARNING] Output directory creation at " + Objects.requireNonNull(getClass().getResource("/output")).getPath().replace("%20", " ") + " was unsuccessful.");
            }
        }

        File newFile = new File(directory, String.format("%s_trial%d_output.txt", instance.getInstanceName(), trial));
        if (newFile.exists()) {
            if (newFile.exists()) {
                new FileWriter(newFile, false).close();
            }
            System.out.println("[INFO] File already exists, rewriting contents.");
        } else {
            boolean fileSuccess = newFile.createNewFile();

            if (fileSuccess) {
                System.out.println("[INFO] Created new file for output, success: [" + fileSuccess + "].");
            } else {
                throw new Exception("[WARNING] Output file creation at " + newFile.getPath() + " was unsuccessful.");
            }
        }

        FileWriter writer = new FileWriter(newFile);
        for (Pair<Double, Double> pair : instance.getGenerationalScores()) {
            writer.write(String.format("%.4f %.4f\n", pair.getLeft(), pair.getRight()));
        }
        writer.flush();
        writer.close();
    }

    private Map<MemeType, Meme> createMemeMapOfInstance(Random rnd, TestConfig config) {
        HashMap<MemeType, Meme> map = new HashMap<>();

        SelectionHeuristic[] selectionHeuristics = Arrays.stream(config.getSelectionHeuristicChosenIndices()).map(index -> {
            switch (index) {
                case 0 -> {
                    return new TournamentSelectionHeuristic(rnd);
                }
                case 1 -> {
                    return new RouletteWheelSelectionHeuristic(rnd);
                }
            }

            return null;
        }).toArray(SelectionHeuristic[]::new);
        CrossoverHeuristic[] crossoverHeuristics = Arrays.stream(config.getCrossoverHeuristicChosenIndices()).map(index -> {
            switch (index) {
                case 0 -> {
                    return new OnePointCrossoverHeuristic(rnd);
                }
                case 1 -> {
                    return new TwoPointCrossoverHeuristic(rnd);
                }
                case 2 -> {
                    return new UniformCrossoverHeuristic(rnd);
                }
            }

            return null;
        }).toArray(CrossoverHeuristic[]::new);
        MutationHeuristic[] mutationHeuristics = Arrays.stream(config.getMutationHeuristicChosenIndices()).map(index -> {
            switch (index) {
                case 0 -> {
                    return new BitMutationHeuristic(rnd);
                }
                case 1 -> {
                    return new SwapMutationHeuristic(rnd);
                }
                case 2 -> {
                    return new InversionMutationHeuristic(rnd);
                }
            }

            return null;
        }).toArray(MutationHeuristic[]::new);
        ReplacementHeuristic[] replacementHeuristics = Arrays.stream(config.getReplacementHeuristicChosenIndices()).map(index -> {
            switch (index) {
                case 0 -> {
                    return new TGAReplacementWithElitismHeuristic(rnd);
                }
            }

            return null;
        }).toArray(ReplacementHeuristic[]::new);
        InheritanceHeuristic[] inheritanceHeuristics = Arrays.stream(config.getInheritanceHeuristicChosenIndices()).map(index -> {
            switch (index) {
                case 0 -> {
                    return new SimpleInheritance(rnd);
                }
            }

            return null;
        }).toArray(SimpleInheritance[]::new);
        LocalSearchHeuristic[] localSearchHeuristics = Arrays.stream(config.getLocalSearchHeuristicChosenIndices()).map(index -> {
            switch (index) {
                case 0 -> {
                    return new DavisBitHillClimbingHeuristic(rnd, MoveAcceptance.IMPROVEMENT_ONLY);
                }
                case 1 -> {
                    return new DavisBitHillClimbingHeuristic(rnd, MoveAcceptance.NON_WORSENING);
                }
                case 2 -> {
                    return new SteepestHillClimbingHeuristic(rnd, MoveAcceptance.IMPROVEMENT_ONLY);
                }
                case 3 -> {
                    return new SteepestHillClimbingHeuristic(rnd, MoveAcceptance.NON_WORSENING);
                }
            }

            return null;
        }).filter(Objects::nonNull).toArray(LocalSearchHeuristic[]::new);

        map.putIfAbsent(MemeType.INTENSITY_OF_MUTATION, new Meme<>(config.getIntensitiesOfMutation()));
        map.putIfAbsent(MemeType.SELECTION, new Meme<>(selectionHeuristics));
        map.putIfAbsent(MemeType.CROSSOVER, new Meme<>(crossoverHeuristics));
        map.putIfAbsent(MemeType.INHERITANCE, new Meme<>(inheritanceHeuristics));
        map.putIfAbsent(MemeType.LOCAL_SEARCH, new Meme<>(localSearchHeuristics));
        map.putIfAbsent(MemeType.MUTATION, new Meme<>(mutationHeuristics));
        map.putIfAbsent(MemeType.REPLACEMENT, new Meme<>(replacementHeuristics));

        return map;
    }
}