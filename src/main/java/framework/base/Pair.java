package framework.base;

public class Pair<T1, T2> {
    private final T1 value1;
    private final T2 value2;

    public Pair(T1 value1, T2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T1 getLeft() {
        return value1;
    }

    public T2 getRight() {
        return value2;
    }
}
