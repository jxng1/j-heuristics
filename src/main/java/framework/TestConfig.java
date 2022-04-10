package framework;

import framework.base.InitialSolutionGeneration;
import framework.base.meme.Meme;
import framework.base.meme.MemeType;
import framework.operators.InfeasibleSolutionsOperator;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class TestConfig {
    private static final String CONFIG_RESOURCE_NAME = "/config.properties";
    private static TestConfig instance;

    protected int trialsCount;
    protected int populationSize;
    protected int runtime;
    protected int maxGenerations;
    protected InfeasibleSolutionsOperator infeasibleSolution;
    protected InitialSolutionGeneration initialSolutionGeneration;
    protected double depthOfSearch;
    protected Map<MemeType, Meme[]> memeMap;

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
            maxGenerations = Integer.parseInt(configProperties.getProperty("max_generations"));
            depthOfSearch = Double.parseDouble(configProperties.getProperty("depth_of_search"));

            if (configProperties.getProperty("infeasible_solution").equals("penalty")) {
                infeasibleSolution = InfeasibleSolutionsOperator.PENALTY;
            } else if (configProperties.getProperty("infeasible_solution").equals("repair")) {
                infeasibleSolution = InfeasibleSolutionsOperator.REPAIR;
            } else {
                throw new IllegalArgumentException("The property 'infeasible_solution' is not correct, please check config.properties for the correct choices.");
            }

            if (configProperties.getProperty("initial_generation").equals("random")) {
                initialSolutionGeneration = InitialSolutionGeneration.RANDOM;
            } else if (configProperties.getProperty("initial_generation").equals("constructive")) {
                initialSolutionGeneration = InitialSolutionGeneration.CONSTRUCTIVE;
            } else if (configProperties.getProperty("initial_generation").equals("greedy")) {
                initialSolutionGeneration = InitialSolutionGeneration.GREEDY;
            } else {
                throw new IllegalArgumentException("The property 'initial_generation' is not correct, please check config.properties for the correct choices.");
            }

            switch (configProperties.getProperty(""))
        } catch (IOException e) {
            System.err.println("[WARNING] Config loading failed." + e);
        } catch (NumberFormatException e) {
            System.err.println("[WARNING] Number property reading failed." + e);
        } catch (IllegalArgumentException e) {
            System.err.println("[WARNING] Property reading failed." + e);
        }
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

    public InfeasibleSolutionsOperator getInfeasibleSolution() {
        return infeasibleSolution;
    }

    public InitialSolutionGeneration getInitialSolutionGeneration() {
        return initialSolutionGeneration;
    }

    public Meme[] getHeuristicsOfType(MemeType type) {
        return memeMap.get(type);
    }

    public Map<MemeType, Meme[]> getMemeMap() {
        return memeMap;
    }
}

