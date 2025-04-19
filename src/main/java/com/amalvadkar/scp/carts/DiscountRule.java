package com.amalvadkar.scp.carts;

import java.math.BigDecimal;

public record DiscountRule(int quantity, BigDecimal price) {
    public static DiscountRule from(int quantity, BigDecimal price) {
        return new DiscountRule(quantity, price);
    }
}
