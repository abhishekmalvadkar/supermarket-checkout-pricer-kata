package com.amalvadkar.scp.products;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    public static Product get(String productCode) {
        return products.stream()
                .filter(by(productCode))
                .findFirst()
                .orElseThrow(productNotFoundExceptionWith(productCode));
    }

    private static Predicate<Product> by(String productCode) {
        return product -> Objects.equals(product.code(), productCode);
    }

    private static boolean productAlreadyExistsWithGiven(String productCode) {
        return products.stream().anyMatch(by(productCode));
    }

    private static Supplier<ProductNotFoundException> productNotFoundExceptionWith(String productCode) {
        return () -> ProductNotFoundException.of(ProductCode.of(productCode));
    }

    private static void throwIfProductAlreadyExistsWithGiven(String productCode) {
        if (productAlreadyExistsWithGiven(productCode)) {
            throw ProductAlreadyExistsException.of(ProductCode.of(productCode));
        }
    }
}
