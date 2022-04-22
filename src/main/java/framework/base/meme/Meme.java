package framework.base.meme;

import java.util.Random;

public class Meme<T> {
    private final T[] options;
    private int currentOption;
    private final int totalOptions;

    public Meme(T[] options) {
        this.options = options;
        this.totalOptions = options.length;
        this.currentOption = new Random().nextInt(totalOptions);
    }

    public void setCurrentOption(int currentOption) {
        if (currentOption >= totalOptions) {
            throw new IllegalArgumentException("Index exceeds amount of options.");
        }
        this.currentOption = currentOption;
    }

    public T getValueOfCurrentIndex() {
        return options[currentOption];
    }

    public Meme<T> deepCopy() {
        return new Meme<>(options);
    }

    public int getCurrentIndex() {
        return currentOption;
    }

    public int getTotalOptions() {
        return totalOptions;
    }
}
