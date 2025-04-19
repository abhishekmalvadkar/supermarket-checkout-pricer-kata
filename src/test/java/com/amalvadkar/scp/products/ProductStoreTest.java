package com.amalvadkar.scp.products;

import com.amalvadkar.scp.common.AbstractTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductStoreTest extends AbstractTest {

    @BeforeEach
    void setUp() {
        ProductStore.clear();
    }

    @Test
    void should_add_new_product_to_store_with_code_and_price() {
        ProductStore.add("P001", new BigDecimal("100"));
        Product product = ProductStore.get("P001");
        assertThat(product.code()).isEqualTo("P001");
        assertThat(product.price()).isEqualTo(new BigDecimal("100"));
        assertThat(ProductStore.items()).hasSize(1);
    }

    @Test
    void should_reject_add_new_product_if_product_already_exists_with_given_code() {
        ProductStore.add("P001", new BigDecimal("100"));
        assertThat(ProductStore.items()).hasSize(1);
        assertThatThrownBy(() -> ProductStore.add("P001", new BigDecimal("100")))
                .isInstanceOf(ProductAlreadyExistsException.class)
                .hasMessage("product already exists with code P001");
        assertThat(ProductStore.items()).hasSize(1);
    }

    @Test
    void should_return_product_for_given_product_code() {
        ProductStore.add("P002", new BigDecimal("200"));
        Product product = ProductStore.get("P002");
        assertThat(product.code()).isEqualTo("P002");
        assertThat(product.price()).isEqualTo(new BigDecimal("200"));
        assertThat(ProductStore.items()).hasSize(1);
    }
    
}
