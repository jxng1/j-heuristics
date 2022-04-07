package util;

import framework.SATInstance;
import framework.TestConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class InputReader {
    private static InputReader instance;

    private InputReader() {

    }

    public static InputReader getInstance() {
        if (Objects.equals(instance, null)) {
            instance = new InputReader();
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

            return new SATInstance(file.getName()
                    .replace(".txt", ""),
                    new Random(seed), // changed later to use a seed value
                    TestConfig.getInstance().getPopulationSize(),
                    firstLineArr[0],
                    firstLineArr[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}