package com.orderdiscount;

import java.util.List;

public class BogoCosmeticsStrategy implements DiscountStrategy {
    
    @Override
    public double calculateDiscount(List<OrderItem> items, double originalAmount) {
        // 買一送一不影響金額計算，只影響商品數量
        // 折扣金額為 0，但會在 OrderService 中處理商品數量增加
        return 0;
    }

    @Override
    public String getStrategyName() {
        return "Buy One Get One - Cosmetics";
    }
    
    public List<OrderItem> applyBogoEffect(List<OrderItem> items) {
        List<OrderItem> resultItems = new java.util.ArrayList<>();
        
        for (OrderItem item : items) {
            if ("cosmetics".equals(item.getProduct().getCategory())) {
                // 買一送一，數量加 1
                resultItems.add(new OrderItem(item.getProduct(), item.getQuantity() + 1));
            } else {
                resultItems.add(item);
            }
        }
        
        return resultItems;
    }
} 