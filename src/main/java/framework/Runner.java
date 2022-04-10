package framework;

import framework.base.SATInstance;
import framework.base.meme.MemeType;
import framework.heuristic.crossover.CrossoverHeuristic;
import framework.heuristic.crossover.OnePointCrossoverHeuristic;
import framework.heuristic.inheritance.InheritanceHeuristic;
import framework.heuristic.inheritance.SimpleMemeplexInheritanceHeuristic;
import framework.heuristic.localsearch.DavisBitHillClimbing;
import framework.heuristic.localsearch.LocalSearchHeuristic;
import framework.heuristic.localsearch.MoveAcceptance;
import framework.heuristic.mutation.BitMutation;
import framework.heuristic.mutation.MutationHeuristic;
import framework.heuristic.replacement.ReplacementHeuristic;
import framework.heuristic.replacement.TGAReplacementWithElitismHeuristic;
import framework.heuristic.selection.SelectionHeuristic;
import framework.heuristic.selection.TournamentSelectionHeuristic;
import framework.searchmethod.populationbased.MultimemeSearchMethod;
import util.InputReader;

import java.io.File;
import java.util.List;

public class Runner {

    public static final int DEBUG = 1;

    public static List<File> INSTANCES_FILES;

    public static void main(String[] args) {
        INSTANCES_FILES = InputReader.getInstance().getInputAsList();
        TestConfig config = TestConfig.getInstance();

        for (File file : INSTANCES_FILES) {
            // run instance x times, specified from config
            for (int i = 0; i < config.getTrialsCount(); i++) {
                SATInstance instance = InputReader
                        .getInstance()
                        .readInstanceFromFile(file, System.currentTimeMillis());

                SelectionHeuristic[] selectionHeuristics = new SelectionHeuristic[]{new TournamentSelectionHeuristic(instance.getRandom())};
                CrossoverHeuristic[] crossoverHeuristics = new CrossoverHeuristic[]{new OnePointCrossoverHeuristic(instance.getRandom())};
                MutationHeuristic[] mutationHeuristics = new MutationHeuristic[]{new BitMutation(instance.getRandom(), config.getIntensityOfMutation() / instance.getNumberOfVariables())};
                ReplacementHeuristic[] replacementHeuristics = new ReplacementHeuristic[]{new TGAReplacementWithElitismHeuristic(instance.getRandom())};
                InheritanceHeuristic[] inheritanceHeuristics = new InheritanceHeuristic[]{new SimpleMemeplexInheritanceHeuristic(instance.getRandom())};
                LocalSearchHeuristic[] localSearchHeuristics = new LocalSearchHeuristic[]{new DavisBitHillClimbing(instance.getRandom(), MoveAcceptance.IMPROVEMENT_ONLY),
                        new DavisBitHillClimbing(instance.getRandom(), MoveAcceptance.NON_WORSENING)};

                MultimemeSearchMethod multimemeSearchMethod = new MultimemeSearchMethod(instance,
                        instance.getRandom(),
                        config.getPopulationSize(),
                        config.getHeuristicsOfType(MemeType.INTENSITY_OF_MUTATION),
                        selectionHeuristics,
                        crossoverHeuristics,
                        mutationHeuristics,
                        replacementHeuristics,
                        inheritanceHeuristics,
                        localSearchHeuristics);

                int generationsCount = 0;
                long t0 = System.currentTimeMillis();
                while ((System.currentTimeMillis() - t0) <= config.getRuntime() &&
                        generationsCount <= config.getMaxGenerations()) {
                    multimemeSearchMethod.run();
                    generationsCount++;

                    if (DEBUG == 1) {
                        System.out.println("Current Best Score: " + instance.getBestObjectiveValue());
                    }
                }
            }
        }
    }

    private static void run() {
        TestConfig config = TestConfig.getInstance();
    }
}