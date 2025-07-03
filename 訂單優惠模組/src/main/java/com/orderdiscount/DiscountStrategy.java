package com.orderdiscount;

import java.util.List;

public interface DiscountStrategy {
    double calculateDiscount(List<OrderItem> items, double originalAmount);
    String getStrategyName();
} 