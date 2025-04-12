package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.products.Product;
import com.amalvadkar.scp.products.ProductStore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {

    private final List<OrderItem> orderItems = new ArrayList<>();

    public BigDecimal totalAmount() {
        return orderItems.stream()
                .map(orderItem -> {
                    int quantity = orderItem.quantity();
                    Product product = orderItem.product();
                    return product.price().multiply(new BigDecimal(Integer.toString(quantity)));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void scan(String productCode) {
        Optional<OrderItem> itemInCartOpt = orderItems.stream()
                .filter(orderItem -> orderItem.product().code().equals(productCode))
                .findFirst();
        if (itemInCartOpt.isPresent()) {
            OrderItem orderItem = itemInCartOpt.get();
            orderItems.remove(orderItem);
            orderItem.quantity(orderItem.quantity() + 1);
            orderItems.add(orderItem);
        } else {
            Product product = ProductStore.get(productCode);
            orderItems.add(new OrderItem(product));
        }
    }

    public int totalItems() {
        return orderItems.size();
    }
}
