package com.amalvadkar.scp.products;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductStore {

    private static final List<Product> products = new ArrayList<>();

    public static void clear() {
        products.clear();
    }

    public static List<Product> items() {
        return products;
    }

    public static void add(String productCode, BigDecimal productPrice) {
        throwIfProductAlreadyExistsWithGiven(productCode);
        products.add(new Product(productCode, productPrice));
    }

    private static void throwIfProductAlreadyExistsWithGiven(String productCode) {
        if (productAlreadyExistsWithGiven(productCode)) {
            throw new ProductAlreadyExistsException(ProductCode.of(productCode));
        }
    }

    public static Product get(String productCode) {
        return products.stream()
                .filter(product -> product.code().equals(productCode))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(String.format("product does not exists for code %s", productCode)));
    }

    private static boolean productAlreadyExistsWithGiven(String productCode) {
        return products.stream()
                .anyMatch(product -> product.code().equals(productCode));
    }
}
