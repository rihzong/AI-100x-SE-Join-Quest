package com.orderdiscount;

public class Product {
    private String name;
    private String category;
    private double unitPrice;

    public Product(String name, String category, double unitPrice) {
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
} 