package com.orderdiscount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@DisplayName("訂單折扣服務單元測試")
public class OrderServiceUnitTest {
    
    private OrderService orderService;
    
    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }
    
    @Test
    @DisplayName("情境1: 無優惠 - 10個襪子，單價100")
    void testNoPromotion() {
        // Given
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("襪子", "apparel", 100), 10));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(1000.0, order.getOriginalAmount(), 0.01);
        assertEquals(0.0, order.getDiscount(), 0.01);
        assertEquals(1000.0, order.getTotalAmount(), 0.01);
        assertEquals(1, order.getItems().size());
        assertEquals(10, order.getItems().get(0).getQuantity());
    }
    
    @Test
    @DisplayName("情境2: 雙十一門檻折扣（基於數量）- 10個襪子，數量達10個折扣20%")
    void testDoubleElevenQuantityDiscount() {
        // Given
        orderService.configureThresholdDiscount(10, 0.2);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("襪子", "apparel", 100), 10));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(1000.0, order.getOriginalAmount(), 0.01);
        assertEquals(200.0, order.getDiscount(), 0.01);
        assertEquals(800.0, order.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("情境3: 雙十一門檻折扣（基於數量）- 12個襪子，前10個有折扣")
    void testDoubleElevenQuantityDiscount12Items() {
        // Given
        orderService.configureThresholdDiscount(10, 0.2);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("襪子", "apparel", 100), 12));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(1200.0, order.getOriginalAmount(), 0.01);
        assertEquals(200.0, order.getDiscount(), 0.01); // 只有前10個有折扣
        assertEquals(1000.0, order.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("情境4: 雙十一門檻折扣（基於數量）- 27個襪子，每10個一組折扣")
    void testDoubleElevenQuantityDiscount27Items() {
        // Given
        orderService.configureThresholdDiscount(10, 0.2);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("襪子", "apparel", 100), 27));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(2700.0, order.getOriginalAmount(), 0.01);
        assertEquals(400.0, order.getDiscount(), 0.01); // 2組10個，每組折扣200
        assertEquals(2300.0, order.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("情境5: 多商品但無折扣 - 每種商品1個，共10種")
    void testMultipleProductsNoDiscount() {
        // Given
        orderService.configureThresholdDiscount(10, 0.2);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("襪子", "apparel", 100), 1));
        items.add(new OrderItem(new Product("T-shirt", "apparel", 100), 1));
        items.add(new OrderItem(new Product("口紅", "cosmetics", 100), 1));
        items.add(new OrderItem(new Product("鞋子", "apparel", 100), 1));
        items.add(new OrderItem(new Product("褲子", "apparel", 100), 1));
        items.add(new OrderItem(new Product("裙子", "apparel", 100), 1));
        items.add(new OrderItem(new Product("糖果", "food", 100), 1));
        items.add(new OrderItem(new Product("水", "beverage", 100), 1));
        items.add(new OrderItem(new Product("果汁", "beverage", 100), 1));
        items.add(new OrderItem(new Product("可樂", "beverage", 100), 1));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(1000.0, order.getOriginalAmount(), 0.01);
        assertEquals(0.0, order.getDiscount(), 0.01); // 每種商品數量都不到10個
        assertEquals(1000.0, order.getTotalAmount(), 0.01);
        assertEquals(10, order.getItems().size());
    }
    
    @Test
    @DisplayName("情境6: 一般門檻折扣（基於總金額）- 總金額達1000折扣100")
    void testTotalAmountThresholdDiscount() {
        // Given
        orderService.configureThresholdDiscount(1000, 100);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("T-shirt", "apparel", 500), 2));
        items.add(new OrderItem(new Product("褲子", "apparel", 600), 1));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(1600.0, order.getOriginalAmount(), 0.01);
        assertEquals(100.0, order.getDiscount(), 0.01);
        assertEquals(1500.0, order.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("情境7: 化妝品買一送一 - 多種化妝品")
    void testBogoCosmeticsMultipleProducts() {
        // Given
        orderService.setBogoCosmeticsActive(true);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("口紅", "cosmetics", 300), 1));
        items.add(new OrderItem(new Product("粉底液", "cosmetics", 400), 1));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(700.0, order.getOriginalAmount(), 0.01);
        assertEquals(0.0, order.getDiscount(), 0.01);
        assertEquals(700.0, order.getTotalAmount(), 0.01);
        
        // 檢查商品數量是否翻倍
        List<OrderItem> finalItems = order.getItems();
        assertEquals(2, finalItems.size());
        
        // 找到口紅並檢查數量
        OrderItem lipstick = finalItems.stream()
            .filter(item -> item.getProduct().getName().equals("口紅"))
            .findFirst().orElse(null);
        assertNotNull(lipstick);
        assertEquals(2, lipstick.getQuantity());
        
        // 找到粉底液並檢查數量
        OrderItem foundation = finalItems.stream()
            .filter(item -> item.getProduct().getName().equals("粉底液"))
            .findFirst().orElse(null);
        assertNotNull(foundation);
        assertEquals(2, foundation.getQuantity());
    }
    
    @Test
    @DisplayName("情境8: 化妝品買一送一 - 同商品兩次")
    void testBogoCosmeticsSameProductTwice() {
        // Given
        orderService.setBogoCosmeticsActive(true);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("口紅", "cosmetics", 300), 2));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(600.0, order.getOriginalAmount(), 0.01);
        assertEquals(0.0, order.getDiscount(), 0.01);
        assertEquals(600.0, order.getTotalAmount(), 0.01);
        
        // 檢查商品數量
        List<OrderItem> finalItems = order.getItems();
        assertEquals(1, finalItems.size());
        assertEquals(3, finalItems.get(0).getQuantity()); // 2個變成3個
    }
    
    @Test
    @DisplayName("情境9: 化妝品買一送一 - 混合品類")
    void testBogoCosmeticsMixedCategories() {
        // Given
        orderService.setBogoCosmeticsActive(true);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("襪子", "apparel", 100), 1));
        items.add(new OrderItem(new Product("口紅", "cosmetics", 300), 1));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(400.0, order.getOriginalAmount(), 0.01);
        assertEquals(0.0, order.getDiscount(), 0.01);
        assertEquals(400.0, order.getTotalAmount(), 0.01);
        
        // 檢查商品數量
        List<OrderItem> finalItems = order.getItems();
        assertEquals(2, finalItems.size());
        
        // 襪子數量不變
        OrderItem socks = finalItems.stream()
            .filter(item -> item.getProduct().getName().equals("襪子"))
            .findFirst().orElse(null);
        assertNotNull(socks);
        assertEquals(1, socks.getQuantity());
        
        // 口紅數量翻倍
        OrderItem lipstick = finalItems.stream()
            .filter(item -> item.getProduct().getName().equals("口紅"))
            .findFirst().orElse(null);
        assertNotNull(lipstick);
        assertEquals(2, lipstick.getQuantity());
    }
    
    @Test
    @DisplayName("情境10: 多重優惠疊加 - 門檻折扣 + 化妝品買一送一")
    void testMultiplePromotionsStacked() {
        // Given
        orderService.configureThresholdDiscount(1000, 100);
        orderService.setBogoCosmeticsActive(true);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("T-shirt", "apparel", 500), 3));
        items.add(new OrderItem(new Product("口紅", "cosmetics", 300), 1));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(1800.0, order.getOriginalAmount(), 0.01);
        assertEquals(100.0, order.getDiscount(), 0.01);
        assertEquals(1700.0, order.getTotalAmount(), 0.01);
        
        // 檢查商品數量
        List<OrderItem> finalItems = order.getItems();
        assertEquals(2, finalItems.size());
        
        // T-shirt 數量不變
        OrderItem tshirt = finalItems.stream()
            .filter(item -> item.getProduct().getName().equals("T-shirt"))
            .findFirst().orElse(null);
        assertNotNull(tshirt);
        assertEquals(3, tshirt.getQuantity());
        
        // 口紅數量翻倍
        OrderItem lipstick = finalItems.stream()
            .filter(item -> item.getProduct().getName().equals("口紅"))
            .findFirst().orElse(null);
        assertNotNull(lipstick);
        assertEquals(2, lipstick.getQuantity());
    }
    
    @Test
    @DisplayName("情境11: 邊界測試 - 數量剛好達到門檻")
    void testBoundaryTestExactThreshold() {
        // Given
        orderService.configureThresholdDiscount(10, 0.2);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("襪子", "apparel", 100), 10));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(1000.0, order.getOriginalAmount(), 0.01);
        assertEquals(200.0, order.getDiscount(), 0.01);
        assertEquals(800.0, order.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("情境12: 邊界測試 - 數量差1個達到門檻")
    void testBoundaryTestOneBelowThreshold() {
        // Given
        orderService.configureThresholdDiscount(10, 0.2);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(new Product("襪子", "apparel", 100), 9));
        
        // When
        Order order = orderService.calculateOrder(items);
        
        // Then
        assertEquals(900.0, order.getOriginalAmount(), 0.01);
        assertEquals(0.0, order.getDiscount(), 0.01); // 沒有折扣
        assertEquals(900.0, order.getTotalAmount(), 0.01);
    }
} 