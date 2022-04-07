package framework;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class TestConfig {
    private static final String CONFIG_RESOURCE_NAME = "/config.properties";
    private static TestConfig instance;

    protected int trialsCount;
    protected int populationSize;
    protected int runtime;
    protected int maxGenerations;
    protected double innovationRate;

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
            innovationRate = Double.parseDouble(configProperties.getProperty("innovation_rate"));


        } catch (IOException e) {
            System.err.println("[WARNING] Config loading failed." + e);
        } catch (NumberFormatException e) {
            System.err.println("[WARNING] Number property reading failed." + e);
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

    public double getInnovationRate() {
        return innovationRate;
    }
}
