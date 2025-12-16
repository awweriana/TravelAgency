package org.example.strategy;

public class CombinedDiscountStrategy implements DiscountStrategy {
    @Override
    public DiscountResult calculate(double originalPrice, double userDiscount, double adminDiscount) {
        double priceAfterUserDiscount = originalPrice * (1 - userDiscount / 100.0);
        double finalPrice = priceAfterUserDiscount * (1 - adminDiscount / 100.0);

        // округляем до 2 знаков после запятой
        finalPrice = Math.round(finalPrice * 100.0) / 100.0;

        double discountAmount = originalPrice - finalPrice;
        discountAmount = Math.round(discountAmount * 100.0) / 100.0;

        double discountPercent = (originalPrice > 0) ? (discountAmount / originalPrice) * 100 : 0;
        discountPercent = Math.round(discountPercent * 10.0) / 10.0;

        return new DiscountResult(finalPrice, discountAmount, discountPercent);
    }
}
