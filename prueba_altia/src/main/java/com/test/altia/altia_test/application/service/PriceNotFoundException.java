package com.test.altia.altia_test.application.service;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(Long productId, Long brandId) {
        super("No applicable price found for product %d and brand %d".formatted(productId, brandId));
    }
}
