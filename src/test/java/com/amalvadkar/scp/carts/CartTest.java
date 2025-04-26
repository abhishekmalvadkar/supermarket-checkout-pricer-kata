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

    @Test
    void should_generate_receipt_with_one_product_scan_and_no_discount() {
        ProductStore.add("P001", new BigDecimal("50"));

        Cart cart = new Cart();
        cart.scan("P001");

        assertThat(cart.receipt()).isEqualTo("""
                Receipt
                
                Product: P001, Quantity: 1, Unit Price: 50, Subtotal: 50
                
                TOTAL AMOUNT DUE: 50
                """);
    }

    @Test
    void should_generate_receipt_with_multiple_product_scan_and_no_discount() {
        ProductStore.add("P001", new BigDecimal("50"));
        ProductStore.add("P002", new BigDecimal("100"));

        Cart cart = new Cart();
        cart.scan("P001");
        cart.scan("P002");

        assertThat(cart.receipt()).isEqualTo("""
                Receipt
                
                Product: P001, Quantity: 1, Unit Price: 50, Subtotal: 50
                Product: P002, Quantity: 1, Unit Price: 100, Subtotal: 100
                
                TOTAL AMOUNT DUE: 150
                """);
    }

    @Test
    void should_generate_receipt_with_multiple_product_scan_with_discount() {
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

        assertThat(cart.receipt()).isEqualTo("""
                Receipt
                
                Product: P001, Quantity: 3, Unit Price: 50, Subtotal: 130 (Applied discount: 3 for 130)
                Product: P002, Quantity: 2, Unit Price: 30, Subtotal: 45 (Applied discount: 2 for 45)
                Product: P003, Quantity: 1, Unit Price: 20, Subtotal: 20
                Product: P004, Quantity: 1, Unit Price: 15, Subtotal: 15
                
                TOTAL AMOUNT DUE: 210
                """);
    }

    @Test
    void should_generate_receipt_with_multiple_product_scan_with_discount_for_some_quantity() {
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

        assertThat(cart.receipt()).isEqualTo("""
                Receipt
                
                Product: P001, Quantity: 4, Unit Price: 50, Subtotal: 180 (Applied discount: 3 for 130)
                Product: P002, Quantity: 3, Unit Price: 30, Subtotal: 75 (Applied discount: 2 for 45)
                Product: P003, Quantity: 1, Unit Price: 20, Subtotal: 20
                Product: P004, Quantity: 1, Unit Price: 15, Subtotal: 15
                
                TOTAL AMOUNT DUE: 290
                """);
    }

    @Test
    void should_generate_receipt_with_multiple_product_scan_with_discount_for_multiple_quantity() {
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

        assertThat(cart.receipt()).isEqualTo("""
                Receipt
                
                Product: P001, Quantity: 6, Unit Price: 50, Subtotal: 260 (Applied discount: 3 for 130)
                Product: P002, Quantity: 4, Unit Price: 30, Subtotal: 90 (Applied discount: 2 for 45)
                Product: P003, Quantity: 1, Unit Price: 20, Subtotal: 20
                Product: P004, Quantity: 1, Unit Price: 15, Subtotal: 15
                
                TOTAL AMOUNT DUE: 385
                """);
    }
}