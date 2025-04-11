package com.amalvadkar.scp.products;

public class ProductAlreadyExistsException extends RuntimeException{

    private ProductAlreadyExistsException(String message) {
        super(message);
    }

    public static ProductAlreadyExistsException of(ProductCode productCode) {
        return new ProductAlreadyExistsException("product already exists with code %s"
                .formatted(productCode.code()));
    }
}
