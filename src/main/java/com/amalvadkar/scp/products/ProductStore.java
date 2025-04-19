package com.amalvadkar.scp.products;

import com.amalvadkar.scp.carts.DiscountRule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

public class ProductStore {

    private static final Map<String,Product> PRODUCT_CODE_TO_PRODUCT_MAP = new ConcurrentHashMap<>();
    private static final Map<String, List<DiscountRule>> PRODUCT_CODE_TO_DISCOUNT_RULES = new ConcurrentHashMap<>();

    public static void clear() {
        PRODUCT_CODE_TO_PRODUCT_MAP.clear();
    }

    public static List<Product> items() {
        return PRODUCT_CODE_TO_PRODUCT_MAP.values().stream().toList();
    }

    public static void add(String productCode, BigDecimal productPrice) {
        throwIfProductAlreadyExistsWithGiven(productCode);
        PRODUCT_CODE_TO_PRODUCT_MAP.put(productCode,new Product(productCode, productPrice));
    }

    public static Product get(String productCode) {
        Product product = PRODUCT_CODE_TO_PRODUCT_MAP.get(productCode);
        if (doesNotExistsForGiveCode(product)) {
            throw ProductNotFoundException.of(ProductCode.of(productCode));
        }
        return product;
    }

    private static boolean doesNotExistsForGiveCode(Product product) {
        return isNull(product);
    }

    private static boolean productAlreadyExistsWithGiven(String productCode) {
        return PRODUCT_CODE_TO_PRODUCT_MAP.containsKey(productCode);
    }

    private static void throwIfProductAlreadyExistsWithGiven(String productCode) {
        if (productAlreadyExistsWithGiven(productCode)) {
            throw ProductAlreadyExistsException.of(ProductCode.of(productCode));
        }
    }

    public static void addDiscountRules(String productCode , List<DiscountRule> discountRules) {
        PRODUCT_CODE_TO_DISCOUNT_RULES.put(productCode, discountRules);
    }

    public static List<DiscountRule> discountRulesFor(String productCode) {
        return PRODUCT_CODE_TO_DISCOUNT_RULES.get(productCode);
    }
}
