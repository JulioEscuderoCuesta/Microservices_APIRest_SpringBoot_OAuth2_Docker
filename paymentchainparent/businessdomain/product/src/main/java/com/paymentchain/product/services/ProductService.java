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
import java.util.stream.Collectors;
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
    
    public ProductDTO getProductById(Long id) {
        Product product = findProduct(id);
        return convertToDTO(product);
    }
    
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO) 
                .collect(Collectors.toList()); 
    }
    
    public List<ProductDTO> getProductsByIds(List<Long> productIds) {        
        List<Product> products = productRepository.findByIdIn(productIds);
        
        // Convert to DTOs
        List<ProductDTO> productDTOs = products.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return productDTOs;
    }
    
    public Product createProduct(ProductDTO productDTO) {
        try {
            Product product = convertToEntity(productDTO);
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation creating product: {}", e.getMessage());
            throw new ProductException(
                "PRODUCT_DATA_CONFLICT",
                "Product data violates constraints: " + e.getMessage(),
                HttpStatus.CONFLICT
            );
        } catch (Exception e) {
            log.error("Unexpected error creating product", e);
            throw new ProductException(
                "PRODUCT_CREATION_ERROR",
                "Failed to create product: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    } 
    
    public Product updateProduct(Long id, ProductDTO productDto) {
        Product product = findProduct(id);
        
        if (productDto.getCode() != null && !product.getName().equals(productDto.getName())) {
            product.setCode(productDto.getCode());
        }
        
        if (productDto.getName() != null && !product.getName().equals(productDto.getName())) {
            product.setName(productDto.getName());
        }
        
        if (productDto.getPrice() != null && productDto.getPrice() > 0) {
            product.setPrice(productDto.getPrice());
        }
                
        return productRepository.save(product);
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
    
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setCode(product.getCode());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }
    
    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCode(productDTO.getCode());
        product.setPrice(productDTO.getPrice());
        return product;
    }
}
