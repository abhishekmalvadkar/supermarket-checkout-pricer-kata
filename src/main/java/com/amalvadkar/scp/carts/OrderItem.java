package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.products.Product;

public record OrderItem(Product product, int quantity, DiscountRule discountRule) {
    public OrderItem(Product product) {
        this(product, 1, null);
    }

    public OrderItem incrementQuantity(){
        return new OrderItem(product, quantity + 1, discountRule);
    }

    public OrderItem decrementQuantity(){
        return new OrderItem(product, quantity - 1, discountRule);
    }

    public OrderItem withDiscountRule(DiscountRule discountRule) {
        return new OrderItem(product, quantity, discountRule);
    }

    public OrderItem removeDiscountRule() {
        return new OrderItem(product, quantity, null);
    }
}
