package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.products.Product;
import com.amalvadkar.scp.products.ProductStore;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {

    private final Map<String, OrderItem> productCodeToOrderItemMap = new ConcurrentHashMap<>();

    public BigDecimal totalAmount() {
        return productCodeToOrderItemMap.values().stream()
                .map(Cart::calculatePriceByQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculatePriceByQuantity(OrderItem orderItem) {
        BigDecimal quantity = new BigDecimal(Integer.toString(orderItem.quantity()));
        BigDecimal price = orderItem.product().price();
        return price.multiply(quantity);
    }

    public void scan(String productCode) {
        if (itemPresentInCartWith(productCode)) {
            OrderItem existsingOrderItem = productCodeToOrderItemMap.get(productCode);
            OrderItem updatedQuatityOrderItem = existsingOrderItem.incrementQuantity();
            addToCart(productCode, updatedQuatityOrderItem);
            return;
        }

        Product product = ProductStore.get(productCode);
        OrderItem orderItem = new OrderItem(product);
        addToCart(productCode, orderItem);
    }

    private void addToCart(String productCode, OrderItem orderItem) {
        productCodeToOrderItemMap.put(productCode, orderItem);
    }

    private boolean itemPresentInCartWith(String productCode) {
        return productCodeToOrderItemMap.containsKey(productCode);
    }

    public int totalItems() {
        return productCodeToOrderItemMap.size();
    }
}
