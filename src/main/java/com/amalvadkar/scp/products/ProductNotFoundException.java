package com.amalvadkar.scp.products;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public static ProductNotFoundException of(ProductCode productCode) {
        return new ProductNotFoundException("product does not exists for code %s"
                .formatted(productCode));
    }
}
