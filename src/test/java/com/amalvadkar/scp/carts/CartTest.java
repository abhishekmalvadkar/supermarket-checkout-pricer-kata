package com.amalvadkar.scp.carts;

import com.amalvadkar.scp.common.AbstractTest;
import com.amalvadkar.scp.products.ProductStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest extends AbstractTest {

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

    @Test
    void should_apply_discount_when_product_has_discount() {
        ProductStore.add("P001", new BigDecimal("50"));
        ProductStore.add("P002", new BigDecimal("30"));
        ProductStore.add("P003", new BigDecimal("20"));
        ProductStore.add("P004", new BigDecimal("15"));

        ProductStore.addDiscountRules("P001",
                List.of(DiscountRule.from(3, new BigDecimal("130"))));
        ProductStore.addDiscountRules("P002",
                List.of(DiscountRule.from(2, new BigDecimal(45))));

        Cart cart = new Cart();
        cart.scan("P001");
        cart.scan("P001");
        cart.scan("P001");

        cart.scan("P002");
        cart.scan("P002");

        cart.scan("P003");
        cart.scan("P004");

        assertThat(cart.totalAmount()).isEqualTo(new BigDecimal("210"));
        assertThat(cart.totalItems()).isEqualTo(4);
    }

    @Test
    void should_apply_discount_when_product_has_discount_but_rule_is_applicable_only_for_some_quantity() {
        ProductStore.add("P001", new BigDecimal("50"));
        ProductStore.add("P002", new BigDecimal("30"));
        ProductStore.add("P003", new BigDecimal("20"));
        ProductStore.add("P004", new BigDecimal("15"));

        ProductStore.addDiscountRules("P001",
                List.of(DiscountRule.from(3, new BigDecimal("130"))));
        ProductStore.addDiscountRules("P002",
                List.of(DiscountRule.from(2, new BigDecimal(45))));

        Cart cart = new Cart();
        cart.scan("P001");
        cart.scan("P001");
        cart.scan("P001");
        cart.scan("P001");

        cart.scan("P002");
        cart.scan("P002");
        cart.scan("P002");

        cart.scan("P003");
        cart.scan("P004");

        assertThat(cart.totalAmount()).isEqualTo(new BigDecimal("290"));
        assertThat(cart.totalItems()).isEqualTo(4);
    }

    @Test
    void should_apply_discount_when_product_has_discount_and_discount_quantity_is_multiple() {
        ProductStore.add("P001", new BigDecimal("50"));
        ProductStore.add("P002", new BigDecimal("30"));
        ProductStore.add("P003", new BigDecimal("20"));
        ProductStore.add("P004", new BigDecimal("15"));

        ProductStore.addDiscountRules("P001",
                List.of(DiscountRule.from(3, new BigDecimal("130"))));
        ProductStore.addDiscountRules("P002",
                List.of(DiscountRule.from(2, new BigDecimal(45))));

        Cart cart = new Cart();
        cart.scan("P001");
        cart.scan("P001");
        cart.scan("P001");
        cart.scan("P001");
        cart.scan("P001");
        cart.scan("P001");

        cart.scan("P002");
        cart.scan("P002");
        cart.scan("P002");
        cart.scan("P002");

        cart.scan("P003");
        cart.scan("P004");

        assertThat(cart.totalAmount()).isEqualTo(new BigDecimal("385"));
        assertThat(cart.totalItems()).isEqualTo(4);
    }
}