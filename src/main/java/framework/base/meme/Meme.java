package framework.base.meme;

public class Meme {
    private MemeOption currentOption;
    private MemeType memeType;
    private MemeOption[] options;


    public Meme(MemeOption option, MemeType type, MemeOption[] availableOptions) {
        this.currentOption = option;
        this.memeType = type;
        this.options = availableOptions;
    }

    public void setCurrentOption(MemeOption currentOption) {
        this.currentOption = currentOption;
    }
}
