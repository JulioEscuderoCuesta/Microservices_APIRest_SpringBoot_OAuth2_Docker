/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.services;

import com.paymentchain.product.dtos.ProductDTO;
import com.paymentchain.product.entities.Product;
import com.paymentchain.product.exceptions.ProductException;
import com.paymentchain.product.repositories.ProductRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hp
 */
@Slf4j
@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Product getProductById(Long id) {
        return findProduct(id);
    }
    
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        return products;  
    }
    
    public Product createProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setCode(dto.getCode());
        
        return saveProduct(product);
    } 
    
    public Product updateProduct(Long id, ProductDTO dto) {
        Product product = findProduct(id);
        
        if (dto.getCode() != null && !product.getName().equals(dto.getName())) {
            product.setCode(dto.getCode());
        }
        
        if (dto.getName() != null && !product.getName().equals(dto.getName())) {
            product.setName(dto.getName());
        }
        
        return saveProduct(product);
    }
    
    public Product deleteProductById(Long id) {
        Product product = findProduct(id);
        
        if (product != null)
            productRepository.deleteById(id);
        
        return product;
    }
    
    private Product findProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductException(
                id,
                "PRODUCT_NOT_FOUND",
                "Product not found",
                HttpStatus.NOT_FOUND
            ));
    }
    
    private Product saveProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation creating product: {}", e.getMessage());
            throw new ProductException(
                "PRODUCT_DATA_CONFLICT",
                "Product data violates constraints: " + e.getMessage(),
                HttpStatus.CONFLICT
            );
        } catch (Exception e) {
            log.error("Unexpected error creating customer", e);
            throw new ProductException(
                "PRODUCT_CREATION_ERROR",
                "Failed to create product: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
