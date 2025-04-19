package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.products.Product;
import com.amalvadkar.scp.products.ProductStore;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {

    private final Map<String, OrderItem> productCodeToOrderItemMap = new ConcurrentHashMap<>();

    public BigDecimal totalAmount() {
        return productCodeToOrderItemMap.values().stream()
                .map(Cart::calculatePriceByQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void scan(String productCode) {
        if (itemPresentInCartWith(productCode)) {
            OrderItem existsingOrderItem = findItemInCartBy(productCode);
            OrderItem updatedQuatityOrderItem = existsingOrderItem.incrementQuantity();

            List<DiscountRule> discountRules = ProductStore.discountRulesFor(productCode);
            if (discountRules != null) {
                for (DiscountRule discountRule : discountRules) {
                    int quantity = discountRule.quantity();
                    if (updatedQuatityOrderItem.quantity() % quantity == 0) {
                        OrderItem orderItemWithDiscountRule = updatedQuatityOrderItem.withDiscountRule(discountRule);
                        addToCart(productCode, orderItemWithDiscountRule);
                        return;
                    }
                }
            }

            addToCart(productCode, updatedQuatityOrderItem);
            return;
        }

        Product product = ProductStore.get(productCode);
        OrderItem orderItem = new OrderItem(product);
        addToCart(productCode, orderItem);
    }

    public int totalItems() {
        return productCodeToOrderItemMap.size();
    }

    private OrderItem findItemInCartBy(String productCode) {
        return productCodeToOrderItemMap.get(productCode);
    }

    private void addToCart(String productCode, OrderItem orderItem) {
        productCodeToOrderItemMap.put(productCode, orderItem);
    }

    private boolean itemPresentInCartWith(String productCode) {
        return productCodeToOrderItemMap.containsKey(productCode);
    }

    private static BigDecimal calculatePriceByQuantity(OrderItem orderItem) {
        DiscountRule discountRule = orderItem.discountRule();
        if (discountRule != null) {
            int currentQuantity = orderItem.quantity();
            int discountRuleQuantity = discountRule.quantity();

            int noOfItemUnitPriceApply = currentQuantity % discountRuleQuantity;
            int noOfItemDiscountPriceApply = currentQuantity / discountRuleQuantity;

            BigDecimal unitPrice = orderItem.product().price()
                    .multiply(new BigDecimal(Integer.toString(noOfItemUnitPriceApply)));

            BigDecimal discountPrice = discountRule.price()
                    .multiply(new BigDecimal(Integer.toString(noOfItemDiscountPriceApply)));

            return unitPrice.add(discountPrice);
        }
        BigDecimal quantity = new BigDecimal(Integer.toString(orderItem.quantity()));
        BigDecimal price = orderItem.product().price();
        return price.multiply(quantity);
    }
}
