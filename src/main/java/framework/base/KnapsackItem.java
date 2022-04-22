package framework.base;

public class KnapsackItem {
    private final double price;
    private final double weight;
    private final double priceToWeightRatio;

    public KnapsackItem(double price, double weight) {
        this.price = price;
        this.weight = weight;
        this.priceToWeightRatio = (double) price / weight;
    }

    public double getPrice() {
        return price;
    }

    public double getWeight() {
        return weight;
    }

    public double getPriceToWeightRatio() {
        return priceToWeightRatio;
    }
}
