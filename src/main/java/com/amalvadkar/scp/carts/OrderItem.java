package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.products.Product;

public record OrderItem(Product product, int quantity) {
    public OrderItem(Product product) {
        this(product, 1);
    }

    public OrderItem incrementQuantity(){
        return new OrderItem(product, quantity + 1);
    }

}
