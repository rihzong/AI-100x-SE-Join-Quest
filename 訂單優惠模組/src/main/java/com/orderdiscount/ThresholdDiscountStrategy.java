package com.orderdiscount;

import java.util.List;

public class ThresholdDiscountStrategy implements DiscountStrategy {
    private double thresholdQuantity;
    private double discountRate;

    public ThresholdDiscountStrategy(double thresholdQuantity, double discountRate) {
        this.thresholdQuantity = thresholdQuantity;
        this.discountRate = discountRate;
    }

    @Override
    public double calculateDiscount(List<OrderItem> items, double originalAmount) {
        double totalDiscount = 0;
        
        for (OrderItem item : items) {
            if (item.getQuantity() >= thresholdQuantity) {
                // 每達到門檻數量就有一組折扣
                int discountGroups = (int) (item.getQuantity() / thresholdQuantity);
                double discountedAmount = discountGroups * thresholdQuantity * item.getProduct().getUnitPrice() * discountRate;
                totalDiscount += discountedAmount;
            }
        }
        
        return totalDiscount;
    }

    @Override
    public String getStrategyName() {
        return "Threshold Discount";
    }
} 