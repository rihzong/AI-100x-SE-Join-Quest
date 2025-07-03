package com.orderdiscount;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderItem> items;
    private double originalAmount;
    private double discount;
    private double totalAmount;

    public Order() {
        this.items = new ArrayList<>();
        this.originalAmount = 0;
        this.discount = 0;
        this.totalAmount = 0;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
} 