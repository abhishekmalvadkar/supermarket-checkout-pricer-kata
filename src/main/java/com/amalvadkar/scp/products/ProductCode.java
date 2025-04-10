package com.amalvadkar.scp.products;

public record ProductCode(String code) {
    public static ProductCode of(String code) {
        return new ProductCode(code);
    }
}
