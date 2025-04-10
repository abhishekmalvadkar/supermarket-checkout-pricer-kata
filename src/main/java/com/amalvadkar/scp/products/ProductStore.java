package com.amalvadkar.scp.products;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductStore {

    private static final List<Product> products = new ArrayList<>();
    private static final String PRODUCT_ALREADY_EXISTS_MSG = "product already exists with code %s";

    public static void clear() {
        products.clear();
    }

    public static List<Product> items() {
        return products;
    }

    public static void add(String productCode, BigDecimal productPrice) {
        if (productAlreadyExistsFor(productCode)) {
            throw new ProductAlreadyExistsException(PRODUCT_ALREADY_EXISTS_MSG.formatted(productCode));
        }
        products.add(new Product(productCode, productPrice));
    }

    public static Product get(String productCode) {
        return products.stream()
                .filter(product -> product.code().equals(productCode))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(String.format("product does not exists for code %s", productCode)));
    }

    private static boolean productAlreadyExistsFor(String productCode) {
        return products.stream()
                .anyMatch(product -> product.code().equals(productCode));
    }
}
