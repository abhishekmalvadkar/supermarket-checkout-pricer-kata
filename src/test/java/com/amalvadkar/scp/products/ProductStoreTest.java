package com.amalvadkar.scp.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductStoreTest {

    @BeforeEach
    void setUp() {
        ProductStore.clear();
    }

    @Test
    void should_add_new_product_to_store_with_code_and_price() throws Exception {
        ProductStore productStore = new ProductStore();
        productStore.add("P001", new BigDecimal("100"));
        Product product = productStore.get("P001");
        assertThat(product.code()).isEqualTo("P001");
        assertThat(product.price()).isEqualTo(new BigDecimal("100"));
        assertThat(productStore.items()).hasSize(1);
    }

    @Test
    void should_reject_add_new_product_if_product_already_exists_with_given_code() throws Exception {
        ProductStore productStore = new ProductStore();
        productStore.add("P001", new BigDecimal("100"));
        assertThat(productStore.items()).hasSize(1);
        assertThatThrownBy(() -> productStore.add("P001", new BigDecimal("100")))
                .isInstanceOf(ProductAlreadyExistsException.class)
                .hasMessage("product already exists with code P001");
        assertThat(productStore.items()).hasSize(1);
    }
    
}
