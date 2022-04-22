package framework.searchmethod.populationbased;

import framework.Runner;
import framework.TestConfig;
import framework.base.SATInstance;
import framework.base.Solution;
import framework.base.meme.Meme;
import framework.base.meme.MemeType;
import framework.heuristic.crossover.CrossoverHeuristic;
import framework.heuristic.inheritance.InheritanceHeuristic;
import framework.heuristic.localsearch.LocalSearchHeuristic;
import framework.heuristic.mutation.MutationHeuristic;
import framework.heuristic.replacement.ReplacementHeuristic;
import framework.heuristic.selection.SelectionHeuristic;
import framework.operators.SelectionOperator;

import java.util.Map;
import java.util.Random;

public class MultimemeSearchMethod extends PopulationBasedSearchMethod {
    private final double innovationRate;
    private final SelectionOperator selectionOperator;


    public MultimemeSearchMethod(SATInstance instance,
                                 Random rnd,
                                 int populationSize,
                                 int tournamentSize,
                                 double innovationRate,
                                 SelectionOperator selectionOperator) {
        super(instance, rnd, populationSize, tournamentSize);
        this.innovationRate = innovationRate;
        this.selectionOperator = selectionOperator;
    }

    @Override
    protected void loop() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Solution solution = instance.getSolutionAtIndex(i);

            // operator to deal with same parent selection
            int p1, p2, c1, c2;
            switch (selectionOperator) {
                case SAME_PARENTS_ALLOWED -> {
                    p1 = ((SelectionHeuristic) solution.getMemeOfType(MemeType.SELECTION).getValueOfCurrentIndex()).applyHeuristic(instance, TOURNAMENT_SIZE);
                    p2 = ((SelectionHeuristic) solution.getMemeOfType(MemeType.SELECTION).getValueOfCurrentIndex()).applyHeuristic(instance, TOURNAMENT_SIZE);
                }
                case SAME_PARENTS_NOT_ALLOWED -> {
                    p1 = ((SelectionHeuristic) solution.getMemeOfType(MemeType.SELECTION).getValueOfCurrentIndex()).applyHeuristic(instance, TOURNAMENT_SIZE);
                    p2 = ((SelectionHeuristic) solution.getMemeOfType(MemeType.SELECTION).getValueOfCurrentIndex()).applyHeuristic(instance, TOURNAMENT_SIZE);

                    if (p1 == p2) {
                        p2 = (p2 + 1) % POPULATION_SIZE;
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + selectionOperator);
            }

            if (Runner.DEBUG == 1) {
                System.out.println("Value of P1: " + p1 + " Value of P2: " + p2);
            }

            c1 = p1 + POPULATION_SIZE;
            c2 = (c1 + 1) % (POPULATION_SIZE * 2);

            ((CrossoverHeuristic) solution.getMemeOfType(MemeType.CROSSOVER).getValueOfCurrentIndex()).applyHeuristic(instance, p1, p2, c1, c2);
            ((InheritanceHeuristic) solution.getMemeOfType(MemeType.INHERITANCE).getValueOfCurrentIndex()).applyHeuristic(instance, p1, p2, c1, c2);

            applyMemeplexMutation(p1);
            applyMemeplexMutation(p2);

            applyMutationOnChild(c1);
            applyMutationOnChild(c2);

            // depth of search on the ls
            for (int z = 0; z < TestConfig.getInstance().getDepthOfSearch(); z++) {
                applyLocalSearchOnChild(c1);
                applyLocalSearchOnChild(c2);
            }
        }

        ((ReplacementHeuristic) instance.getCurrentBestSolution().getMemeOfType(MemeType.REPLACEMENT).getValueOfCurrentIndex()).applyHeuristic(instance, POPULATION_SIZE);
//        System.out.println(instance);
        if (Runner.DEBUG == 1) {
            System.out.println("Replacement done based on best solution ever.");
        }
    }

    private void applyLocalSearchOnChild(int childSolutionIndex) {
        ((LocalSearchHeuristic) instance.getSolutionAtIndex(childSolutionIndex).getMemeOfType(MemeType.LOCAL_SEARCH).getValueOfCurrentIndex()).applyHeuristic(instance, childSolutionIndex);
    }

    private void applyMutationOnChild(int childSolutionIndex) {
        ((MutationHeuristic) instance.getSolutionAtIndex(childSolutionIndex).getMemeOfType(MemeType.MUTATION).getValueOfCurrentIndex()).applyHeuristic(instance, childSolutionIndex);
    }

    private void applyMemeplexMutation(int solutionIndex) {
        for (Map.Entry<MemeType, Meme> entry : instance.getSolutionAtIndex(solutionIndex).getMemes().entrySet()) {
            if (rnd.nextDouble() < innovationRate) {
                entry.getValue().setCurrentOption(rnd.nextInt(entry.getValue().getTotalOptions()));
            }
        }
    }
}
