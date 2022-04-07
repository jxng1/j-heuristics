package framework;

import java.util.Random;

public class Solution {
    private Random rnd = null;
    private char[] variables;

    public Solution(Random rnd, char[] values) {
        this.rnd = rnd;
        this.variables = values;
    }

    public char[] getVariables() {
        return variables;
    }

    public void setBits(char[] newArr, int from) {
        if (from < 0 || from + newArr.length > variables.length) {
            return; // should error here
        }

        for (int i = from, ite = 0; i < newArr.length; i++, ite++) {
            variables[i] = newArr[ite];
        }
    }
}
