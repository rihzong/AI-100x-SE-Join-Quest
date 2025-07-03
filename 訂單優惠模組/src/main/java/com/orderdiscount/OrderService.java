package com.orderdiscount;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private List<DiscountStrategy> discountStrategies;
    private BogoCosmeticsStrategy bogoStrategy;

    public OrderService() {
        this.discountStrategies = new ArrayList<>();
        this.bogoStrategy = null;
    }

    public void addDiscountStrategy(DiscountStrategy strategy) {
        discountStrategies.add(strategy);
    }

    public void setBogoCosmeticsActive(boolean active) {
        if (active) {
            this.bogoStrategy = new BogoCosmeticsStrategy();
        } else {
            this.bogoStrategy = null;
        }
    }

    public void configureThresholdDiscount(double threshold, double discount) {
        // 移除舊的門檻折扣策略（如果存在）
        discountStrategies.removeIf(strategy -> 
            strategy instanceof ThresholdDiscountStrategy || 
            strategy instanceof TotalAmountThresholdDiscountStrategy);
        
        // 根據參數判斷是基於數量還是基於總金額的折扣
        if (threshold >= 100) {
            // 如果門檻值很大，認為是基於總金額的折扣
            discountStrategies.add(new TotalAmountThresholdDiscountStrategy(threshold, discount));
        } else {
            // 如果門檻值較小，認為是基於數量的折扣
            discountStrategies.add(new ThresholdDiscountStrategy(threshold, discount));
        }
    }

    public Order calculateOrder(List<OrderItem> items) {
        Order order = new Order();
        
        // 計算原始金額
        double originalAmount = items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        order.setOriginalAmount(originalAmount);

        // 應用買一送一效果（如果啟用）
        List<OrderItem> finalItems = items;
        if (bogoStrategy != null) {
            finalItems = bogoStrategy.applyBogoEffect(items);
        }
        
        // 添加商品到訂單
        for (OrderItem item : finalItems) {
            order.addItem(item);
        }

        // 計算總折扣
        double totalDiscount = 0;
        for (DiscountStrategy strategy : discountStrategies) {
            totalDiscount += strategy.calculateDiscount(items, originalAmount);
        }
        order.setDiscount(totalDiscount);

        // 計算總金額
        order.setTotalAmount(originalAmount - totalDiscount);

        return order;
    }
} 