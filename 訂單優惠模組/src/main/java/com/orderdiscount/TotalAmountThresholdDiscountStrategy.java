package com.orderdiscount;

import java.util.List;

public class TotalAmountThresholdDiscountStrategy implements DiscountStrategy {
    private double thresholdAmount;
    private double discountAmount;

    public TotalAmountThresholdDiscountStrategy(double thresholdAmount, double discountAmount) {
        this.thresholdAmount = thresholdAmount;
        this.discountAmount = discountAmount;
    }

    @Override
    public double calculateDiscount(List<OrderItem> items, double originalAmount) {
        if (originalAmount >= thresholdAmount) {
            return discountAmount;
        }
        return 0;
    }

    @Override
    public String getStrategyName() {
        return "Total Amount Threshold Discount";
    }
} 