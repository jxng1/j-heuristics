package framework.base;

public class KnapsackItem {
    private final int price;
    private final int weight;
    private final double priceToWeightRatio;

    public KnapsackItem(int price, int weight) {
        this.price = price;
        this.weight = weight;
        this.priceToWeightRatio = (double) price / weight;
    }

    public int getPrice() {
        return price;
    }

    public int getWeight() {
        return weight;
    }

    public double getPriceToWeightRatio() {
        return priceToWeightRatio;
    }
}
