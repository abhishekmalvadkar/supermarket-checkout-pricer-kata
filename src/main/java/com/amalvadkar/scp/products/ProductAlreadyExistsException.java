package com.amalvadkar.scp.products;

public class ProductAlreadyExistsException extends RuntimeException{

    public ProductAlreadyExistsException(String message) {
        super(message);
    }

    public static ProductAlreadyExistsException of(ProductCode productCode) {
        return new ProductAlreadyExistsException("product already exists with code %s"
                .formatted(productCode.code()));
    }
}
