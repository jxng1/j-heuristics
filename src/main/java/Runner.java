import framework.SATInstance;
import framework.TestConfig;
import framework.searchmethod.populationbased.Multimeme;
import util.InputReader;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Runner {

    public static List<File> INSTANCES_FILES;

    public static void main(String[] args) {
        INSTANCES_FILES = InputReader.getInstance().getInputAsList();
        TestConfig config = TestConfig.getInstance();

        INSTANCES_FILES.forEach(file ->
                {
                    // or array of seeds
                    SATInstance instance = InputReader
                            .getInstance()
                            .readInstanceFromFile(file, System.currentTimeMillis());
                    instance.generateConstructiveInitialSolution();

                    Multimeme multimeme = new Multimeme(instance,
                            instance.getRandom(),
                            config.getPopulationSize(),
                            config.getInnovationRate());

                    int generationsCount = 0;
                    long t0 = System.currentTimeMillis();
                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                    scheduler.scheduleWithFixedDelay(new Runnable() {
                        @Override
                        public void run() {
                            // if generation count is greater than the max generations then shutdown,
                            // same with the runtime
                            if (generationsCount > config.getMaxGenerations()
                                    || (System.currentTimeMillis() - t0) > config.getRuntime()) {
                                scheduler.shutdownNow();
                            }

                            multimeme.run();
                        }
                    }, 0, 0, TimeUnit.MILLISECONDS);
                }
        );
    }

    private static void run() {
        TestConfig config = TestConfig.getInstance();


    }
}