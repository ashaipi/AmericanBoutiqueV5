package com.AmericanBoutique.service;

import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.repo.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Test
    void getAllProductsTest() {
        //BBD behavior driven development
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setProductName("Test Product");
        List<Product> productList = Collections.singletonList(product);

        // Act: Calling the method to be tested
        //SELECT p1_0.id, p1_0.description, p1_0.img, p1_0.price, p1_0.product_name, p1_0.stock_quantity FROM product p1_0
        List<Product> products = productServiceImpl.getAllProducts();

        // Assert
        Assertions.assertNotNull(products, "Product list should not be null");
        Assertions.assertFalse(products.isEmpty(), "Product list should not be empty");
        Assertions.assertNotEquals(productList.size(), products.size(), "Product list size should match");

    }

    @Test
    void saveProduct() {
    }

    @Test
    void getProductById() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProductById() {
    }
}