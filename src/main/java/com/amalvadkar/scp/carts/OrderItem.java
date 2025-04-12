package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.products.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public final class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

}
