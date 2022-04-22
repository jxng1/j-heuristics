package framework;

import framework.operators.InfeasibleSolutionsRefactorOperator;
import framework.operators.InitialSolutionGenerationOperator;
import framework.operators.SelectionOperator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class TestConfig {
    private static final String CONFIG_RESOURCE_NAME = "/config.properties";
    private static TestConfig instance;

    protected int trialsCount;
    protected int populationSize;
    protected int runtime;
    protected int maxGenerations;
    protected int tournamentSize;
    protected InfeasibleSolutionsRefactorOperator infeasibleSolution;
    protected InitialSolutionGenerationOperator initialSolutionGenerationOperator;
    protected int depthOfSearch;
    protected double innovationRate;
    protected Integer[] intensitiesOfMutation;
    protected Integer[] selectionHeuristicChosenIndices;
    protected Integer[] crossoverHeuristicChosenIndices;
    protected Integer[] inheritanceHeuristicChosenIndices;
    protected Integer[] localSearchHeuristicChosenIndices;
    protected Integer[] mutationHeuristicChosenIndices;
    protected Integer[] replacementHeuristicChosenIndices;
    protected SelectionOperator selectionOperator;

    protected int generationOutputCount;


    private TestConfig() {
        // load properties
        try {
            Properties configProperties = new Properties();
            configProperties
                    .load(TestConfig.class.getResourceAsStream(CONFIG_RESOURCE_NAME));

            trialsCount = Integer.parseInt(configProperties.getProperty("total_runs"));
            populationSize = Integer.parseInt(configProperties.getProperty("population_size"));
            runtime = configProperties.getProperty("runtime").isBlank()
                    ? Integer.MAX_VALUE
                    : Integer.parseInt(configProperties.getProperty("runtime"));
            maxGenerations = configProperties.getProperty("max_generations").isEmpty()
                    ? Integer.MAX_VALUE
                    : Integer.parseInt(configProperties.getProperty("max_generations"));
            depthOfSearch = Integer.parseInt(configProperties.getProperty("depth_of_search"));
            tournamentSize = Integer.parseInt(configProperties.getProperty("tournament_size"));
            innovationRate = Double.parseDouble(configProperties.getProperty("innovation_rate"));

            if (configProperties.getProperty("number_of_generation_output").isBlank()) {
                generationOutputCount = maxGenerations;
            } else {
                generationOutputCount = Integer.parseInt(configProperties.getProperty("number_of_generation_output"));
            }

            if (configProperties.getProperty("selection_operator").equals("same_parents_allowed") || configProperties.getProperty("selection_operator").equals("same_parents_not_allowed")) {
                selectionOperator = (configProperties.getProperty("selection_operator").equals("same_parents_allowed"))
                        ? SelectionOperator.SAME_PARENTS_ALLOWED
                        : SelectionOperator.SAME_PARENTS_NOT_ALLOWED;
            } else {
                throw new IllegalArgumentException("The property 'selection_operator' is not correct, please check config.properties for the correct choices.");
            }

            if (tournamentSize > populationSize || tournamentSize <= 0) {
                throw new IllegalArgumentException("The property 'tournament_size' should not be greater than the property 'population_size' or <= 0.");
            }

            if (configProperties.getProperty("infeasible_solution").equals("penalty")) {
                infeasibleSolution = InfeasibleSolutionsRefactorOperator.PENALTY;
            } else if (configProperties.getProperty("infeasible_solution").equals("repair")) {
                infeasibleSolution = InfeasibleSolutionsRefactorOperator.REPAIR;
            } else {
                throw new IllegalArgumentException("The property 'infeasible_solution' is not correct, please check config.properties for the correct choices.");
            }

            if (configProperties.getProperty("initial_generation").equals("random")) {
                initialSolutionGenerationOperator = InitialSolutionGenerationOperator.RANDOM;
            } else if (configProperties.getProperty("initial_generation").equals("constructive")) {
                initialSolutionGenerationOperator = InitialSolutionGenerationOperator.CONSTRUCTIVE;
            } else if (configProperties.getProperty("initial_generation").equals("greedy")) {
                initialSolutionGenerationOperator = InitialSolutionGenerationOperator.GREEDY;
            } else {
                throw new IllegalArgumentException("The property 'initial_generation' is not correct, please check config.properties for the correct choices.");
            }

            if (!configProperties.getProperty("intensities_of_mutation").isEmpty()) {
                intensitiesOfMutation = Arrays.stream(configProperties.getProperty("intensities_of_mutation").split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
            } else {
                throw new IllegalArgumentException("The property 'intensities_of_mutation' is empty, please check config.properties for the correct choices.");
            }

            if (!configProperties.getProperty("crossover").isEmpty()) {
                crossoverHeuristicChosenIndices = Arrays.stream(configProperties.getProperty("crossover").split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
            } else {
                throw new IllegalArgumentException("The property 'crossover' is empty, please check config.properties for the correct choices.");
            }

            if (!configProperties.getProperty("inheritance").isEmpty()) {
                inheritanceHeuristicChosenIndices = Arrays.stream(configProperties.getProperty("inheritance").split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
            } else {
                throw new IllegalArgumentException("The property 'inheritance' is empty, please check config.properties for the correct choices.");
            }

            if (!configProperties.getProperty("local_search").isEmpty()) {
                localSearchHeuristicChosenIndices = Arrays.stream(configProperties.getProperty("local_search").split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
            } else {
                throw new IllegalArgumentException("The property 'local_search' is empty, please check config.properties for the correct choices.");
            }

            if (!configProperties.getProperty("mutation").isEmpty()) {
                mutationHeuristicChosenIndices = Arrays.stream(configProperties.getProperty("mutation").split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
            } else {
                throw new IllegalArgumentException("The property 'mutation' is empty, please check config.properties for the correct choices.");
            }

            if (!configProperties.getProperty("replacement").isEmpty()) {
                replacementHeuristicChosenIndices = Arrays.stream(configProperties.getProperty("replacement").split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
            } else {
                throw new IllegalArgumentException("The property 'replacement' is empty, please check config.properties for the correct choices.");
            }

            if (!configProperties.getProperty("selection").isEmpty()) {
                selectionHeuristicChosenIndices = Arrays.stream(configProperties.getProperty("selection").split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
            } else {
                throw new IllegalArgumentException("The property 'selection' is empty, please check config.properties for the correct choices.");
            }
        } catch (IOException e) {
            System.err.println("[WARNING] Config loading failed." + e);
        } catch (NumberFormatException e) {
            System.err.println("[WARNING] Number property reading failed." + e);
        } catch (IllegalArgumentException e) {
            System.err.println("[WARNING] Property reading failed." + e);
        }
    }

    public int getGenerationOutputCount() {
        return generationOutputCount;
    }

    public static TestConfig getInstance() {
        if (Objects.equals(instance, null)) {
            instance = new TestConfig();
        }

        return instance;
    }

    public int getTrialsCount() {
        return trialsCount;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getRuntime() {
        return runtime;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public InfeasibleSolutionsRefactorOperator getInfeasibleSolution() {
        return infeasibleSolution;
    }

    public InitialSolutionGenerationOperator getInitialSolutionGeneration() {
        return initialSolutionGenerationOperator;
    }

    public double getInnovationRate() {
        return innovationRate;
    }

    public Integer[] getIntensitiesOfMutation() {
        return intensitiesOfMutation;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public Integer[] getSelectionHeuristicChosenIndices() {
        return selectionHeuristicChosenIndices;
    }

    public InitialSolutionGenerationOperator getInitialSolutionGenerationOperator() {
        return initialSolutionGenerationOperator;
    }

    public double getDepthOfSearch() {
        return depthOfSearch;
    }

    public Integer[] getInheritanceHeuristicChosenIndices() {
        return inheritanceHeuristicChosenIndices;
    }

    public Integer[] getLocalSearchHeuristicChosenIndices() {
        return localSearchHeuristicChosenIndices;
    }

    public Integer[] getMutationHeuristicChosenIndices() {
        return mutationHeuristicChosenIndices;
    }

    public Integer[] getReplacementHeuristicChosenIndices() {
        return replacementHeuristicChosenIndices;
    }

    public Integer[] getCrossoverHeuristicChosenIndices() {
        return crossoverHeuristicChosenIndices;
    }

    public SelectionOperator getSelectionOperator() {
        return selectionOperator;
    }
}