package com.amalvadkar.scp.products;

public class ProductAlreadyExistsException extends RuntimeException{

    public ProductAlreadyExistsException(ProductCode productCode) {
        super("product already exists with code %s".formatted(productCode.code()));
    }
}
