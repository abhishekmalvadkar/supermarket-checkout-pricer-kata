package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.products.Product;
import com.amalvadkar.scp.products.ProductStore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Cart {

    private static final String PRODUCT_LINE_CONTENT_WITHOUT_DISCOUNT = "Product: %s, Quantity: %d, Unit Price: %s, Subtotal: %s";
    private static final String PRODUCT_LINE_CONTENT_WITH_DISCOUNT = PRODUCT_LINE_CONTENT_WITHOUT_DISCOUNT + " (Applied discount: %s for %s)";
    private static final String LINE_BREAK = "\n";

    private final Map<String, OrderItem> productCodeToOrderItemMap = new LinkedHashMap<>();

    public BigDecimal totalAmount() {
        return productCodeToOrderItemMap.values().stream()
                .map(Cart::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void scan(String productCode) {
        if (itemPresentInCartWith(productCode)) {
            OrderItem existsingOrderItem = findItemInCartBy(productCode);
            OrderItem updatedQuatityOrderItem = existsingOrderItem.incrementQuantity();

            if (productHasDiscountRule(productCode)) {
                for (DiscountRule discountRule : ProductStore.discountRulesFor(productCode)) {
                    if (canApplyDiscountBasedOnCurrentQuantity(discountRule, updatedQuatityOrderItem)) {
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

    public String receipt() {
        List<String> productLines = new ArrayList<>();
        for (Map.Entry<String, OrderItem> productEntry : productCodeToOrderItemMap.entrySet()) {
            String productLine = prepareProductLine(productEntry);
            productLines.add(productLine);
        }
        return prepareReceipt(productLines);
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

    private static BigDecimal calculatePrice(OrderItem orderItem) {
        if (productHasDiscountRule(orderItem)) {
            DiscountRule discountRule = orderItem.discountRule();
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

    private static boolean productHasDiscountRule(OrderItem orderItem) {
        return orderItem.discountRule() != null;
    }

    private String prepareReceipt(List<String> productLine) {
        String productsLines = String.join(LINE_BREAK, productLine);
        return """
                Receipt
                
                %s
                
                TOTAL AMOUNT DUE: %s
                """.formatted(productsLines, totalAmount());
    }

    private static String prepareProductLine(Map.Entry<String, OrderItem> productEntry) {
        OrderItem orderItem = productEntry.getValue();
        if (hasDiscount(orderItem)) {
            return productLineWithDiscount(productEntry);
        } else {
            return productLineWithoutDiscount(productEntry);
        }
    }

    private static String productLineWithoutDiscount(Map.Entry<String, OrderItem> productEntry) {
        String productCode = productEntry.getKey();
        OrderItem orderItem = productEntry.getValue();
        Product product = orderItem.product();
        BigDecimal subTotal = calculatePrice(orderItem);
        return PRODUCT_LINE_CONTENT_WITHOUT_DISCOUNT.formatted(productCode,
                orderItem.quantity(),
                product.price(),
                subTotal);
    }

    private static String productLineWithDiscount(Map.Entry<String, OrderItem> productEntry) {
        String productCode = productEntry.getKey();
        OrderItem orderItem = productEntry.getValue();
        Product product = orderItem.product();
        BigDecimal subTotal = calculatePrice(orderItem);
        DiscountRule discountRule = orderItem.discountRule();
        return PRODUCT_LINE_CONTENT_WITH_DISCOUNT.formatted(productCode,
                orderItem.quantity(),
                product.price(),
                subTotal,
                discountRule.quantity(),
                discountRule.price());
    }

    private static boolean hasDiscount(OrderItem orderItem) {
        return Objects.nonNull(orderItem.discountRule());
    }

    private static boolean canApplyDiscountBasedOnCurrentQuantity(DiscountRule discountRule, OrderItem updatedQuatityOrderItem) {
        return updatedQuatityOrderItem.quantity() % discountRule.quantity() == 0;
    }

    private static boolean productHasDiscountRule(String productCode) {
        return ProductStore.discountRulesFor(productCode) != null;
    }
}
