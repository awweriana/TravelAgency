package org.example.strategy;

public class DiscountResult {
    private final double finalPrice;
    private final double discountAmount;
    private final double discountPercent;

    public DiscountResult(double finalPrice, double discountAmount, double discountPercent) {
        this.finalPrice = finalPrice;
        this.discountAmount = discountAmount;
        this.discountPercent = discountPercent;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
