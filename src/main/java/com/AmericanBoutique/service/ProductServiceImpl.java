package com.AmericanBoutique.service;

import com.AmericanBoutique.model.Product;
import com.AmericanBoutique.repo.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

        private final ProductRepository productRepository;

        public ProductServiceImpl(ProductRepository productRepository) {
            super();
            this.productRepository = productRepository;
        }

        @Override
        public List<Product> getAllProducts() {
            return productRepository.findAll();
        }

        @Override
        public Product saveProduct(Product product) {
            return productRepository.save(product);
        }

        @Override
        public Product getProductById(Long id) {
            return productRepository.findById(id).get();
        }

        @Override
        public Product updateProduct(Product product) {
            return productRepository.save(product);
        }

        @Override
        public void deleteProductById(Long id) {
            productRepository.deleteById(id);
        }
}