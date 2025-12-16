package org.example.strategy;

public interface DiscountStrategy {
    DiscountResult calculate(double originalPrice, double userDiscount, double adminDiscount);
}
