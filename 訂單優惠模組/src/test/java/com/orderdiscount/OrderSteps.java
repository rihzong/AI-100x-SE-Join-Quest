package com.orderdiscount;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class OrderSteps {
    private OrderService orderService;
    private List<OrderItem> orderItems;
    private Order calculatedOrder;
    private double thresholdDiscount;
    private double thresholdAmount;
    private boolean bogoCosmeticsActive = false;

    public OrderSteps() {
        this.orderService = new OrderService();
        this.orderItems = new ArrayList<>();
    }

    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        // 暫時不實作，只是一個標記
    }

    @Given("Double Eleven promotions is active And the threshold discount promotion is configured:")
    public void double_eleven_promotions_is_active_and_the_threshold_discount_promotion_is_configured(DataTable dataTable) {
        Map<String, String> config = dataTable.asMaps().get(0);
        this.thresholdAmount = Double.parseDouble(config.get("threshold"));
        this.thresholdDiscount = Double.parseDouble(config.get("discount"));
        orderService.configureThresholdDiscount(thresholdAmount, thresholdDiscount);
    }

    @Given("Double Eleven promotions is active the threshold discount promotion is configured:")
    public void double_eleven_promotions_is_active_the_threshold_discount_promotion_is_configured(DataTable dataTable) {
        Map<String, String> config = dataTable.asMaps().get(0);
        this.thresholdAmount = Double.parseDouble(config.get("threshold"));
        this.thresholdDiscount = Double.parseDouble(config.get("discount"));
        orderService.configureThresholdDiscount(thresholdAmount, thresholdDiscount);
    }

    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(DataTable dataTable) {
        Map<String, String> config = dataTable.asMaps().get(0);
        this.thresholdAmount = Double.parseDouble(config.get("threshold"));
        this.thresholdDiscount = Double.parseDouble(config.get("discount"));
        orderService.configureThresholdDiscount(thresholdAmount, thresholdDiscount);
    }

    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        bogoCosmeticsActive = true;
        orderService.setBogoCosmeticsActive(true);
    }

    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        orderItems.clear();

        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int quantity = Integer.parseInt(row.get("quantity"));
            double unitPrice = Double.parseDouble(row.get("unitPrice"));
            String category = row.containsKey("category") ? row.get("category") : "apparel";

            Product product = new Product(productName, category, unitPrice);
            OrderItem item = new OrderItem(product, quantity);
            orderItems.add(item);
        }

        calculatedOrder = orderService.calculateOrder(orderItems);
    }

    @Then("the order summary should be:")
    public void the_order_summary_should_be(DataTable dataTable) {
        Map<String, String> expected = dataTable.asMaps().get(0);
        
        if (expected.containsKey("totalAmount")) {
            double expectedTotal = Double.parseDouble(expected.get("totalAmount"));
            double actualTotal = calculatedOrder.getTotalAmount();
            assert Math.abs(actualTotal - expectedTotal) < 0.01 :
                "Expected total: " + expectedTotal + ", but got: " + actualTotal;
        } else {
            double expectedOriginal = Double.parseDouble(expected.get("originalAmount"));
            double expectedDiscount = Double.parseDouble(expected.get("discount"));
            double expectedTotal = Double.parseDouble(expected.get("totalAmount"));
            
            double actualOriginal = calculatedOrder.getOriginalAmount();
            double actualDiscount = calculatedOrder.getDiscount();
            double actualTotal = calculatedOrder.getTotalAmount();
            
            assert Math.abs(actualOriginal - expectedOriginal) < 0.01 :
                "Expected original: " + expectedOriginal + ", but got: " + actualOriginal;
            assert Math.abs(actualDiscount - expectedDiscount) < 0.01 :
                "Expected discount: " + expectedDiscount + ", but got: " + actualDiscount;
            assert Math.abs(actualTotal - expectedTotal) < 0.01 :
                "Expected total: " + expectedTotal + ", but got: " + actualTotal;
        }
    }

    @Then("the customer should receive:")
    public void the_customer_should_receive(DataTable dataTable) {
        List<Map<String, String>> expectedItems = dataTable.asMaps();
        List<OrderItem> actualItems = calculatedOrder.getItems();
        assert actualItems.size() == expectedItems.size() :
            "Expected " + expectedItems.size() + " items, but got " + actualItems.size();
        
        for (int i = 0; i < expectedItems.size(); i++) {
            Map<String, String> expected = expectedItems.get(i);
            OrderItem actual = actualItems.get(i);
            
            assert actual.getProduct().getName().equals(expected.get("productName")) :
                "Expected product: " + expected.get("productName") + ", but got: " + actual.getProduct().getName();
            assert actual.getQuantity() == Integer.parseInt(expected.get("quantity")) :
                "Expected quantity: " + expected.get("quantity") + ", but got: " + actual.getQuantity();
        }
    }
} 