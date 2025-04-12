package com.amalvadkar.scp.products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;

public class ProductStore {

    private static final Map<String,Product> PRODUCT_ID_TO_PRODUCT_MAP = new ConcurrentHashMap<>();

    public static void clear() {
        PRODUCT_ID_TO_PRODUCT_MAP.clear();
    }

    public static List<Product> items() {
        return PRODUCT_ID_TO_PRODUCT_MAP.values().stream().toList();
    }

    public static void add(String productCode, BigDecimal productPrice) {
        throwIfProductAlreadyExistsWithGiven(productCode);
        PRODUCT_ID_TO_PRODUCT_MAP.put(productCode,new Product(productCode, productPrice));
    }

    public static Product get(String productCode) {
        Product product = PRODUCT_ID_TO_PRODUCT_MAP.get(productCode);
        if (doesNotExistsForGiveCode(product)) {
            throw ProductNotFoundException.of(ProductCode.of(productCode));
        }
        return product;
    }

    private static boolean doesNotExistsForGiveCode(Product product) {
        return isNull(product);
    }

    private static boolean productAlreadyExistsWithGiven(String productCode) {
        return PRODUCT_ID_TO_PRODUCT_MAP.containsKey(productCode);
    }

    private static void throwIfProductAlreadyExistsWithGiven(String productCode) {
        if (productAlreadyExistsWithGiven(productCode)) {
            throw ProductAlreadyExistsException.of(ProductCode.of(productCode));
        }
    }
}
