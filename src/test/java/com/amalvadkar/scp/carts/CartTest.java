package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.products.ProductStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @BeforeEach
    void setUp() {
        ProductStore.clear();
    }

    @Test
    void should_return_total_amount_zero_if_cart_is_empty() {
        Cart cart = new Cart();

        assertThat(cart.totalAmount()).isZero();
    }

    @Test
    void should_return_total_amount_if_i_scan_two_products() {
        ProductStore.add("P001", new BigDecimal("150"));
        ProductStore.add("P002", new BigDecimal("147.50"));

        Cart cart = new Cart();
        cart.scan("P001");
        cart.scan("P002");

        assertThat(cart.totalAmount()).isEqualTo(new BigDecimal("297.50"));
    }

    @Test
    void should_increase_quantity_of_product_if_i_scan_same_product_again_and_calculate_amount_based_on_that() {
        ProductStore.add("P001", new BigDecimal("150"));
        ProductStore.add("P002", new BigDecimal("147.50"));

        Cart cart = new Cart();
        cart.scan("P001");
        cart.scan("P002");
        cart.scan("P001");

        assertThat(cart.totalAmount()).isEqualTo(new BigDecimal("447.50"));
        assertThat(cart.totalItems()).isEqualTo(2);
    }

}