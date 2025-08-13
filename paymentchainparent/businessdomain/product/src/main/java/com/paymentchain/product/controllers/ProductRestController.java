/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.product.controllers;

import com.paymentchain.product.dtos.ProductDTO;
import com.paymentchain.product.entities.Product;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.product.services.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Hp
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductRestController {
    
    @Autowired
    ProductService productService;
        
    @GetMapping()
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    
    @GetMapping("/batch/{ids}")
    public ResponseEntity<List<ProductDTO>> getProductsBatchPath(@PathVariable String ids) {
    try {
            List<Long> productIds = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());
            
            if (productIds.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Limitar number of products requested
            if (productIds.size() > 100) {
                log.warn("Too many product IDs requested: {}. Limiting to 100.", productIds.size());
                productIds = productIds.subList(0, 100);
            }
            
            List<ProductDTO> products = productService.getProductsByIds(productIds);
            
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(products);
            
        } catch (NumberFormatException e) {
            log.error("Invalid product IDs format: {}", ids);
            return ResponseEntity.badRequest().build();
        }
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }
    
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = productService.createProduct(productDTO);

        URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/product/{id}")
            .buildAndExpand(product.getId())
            .toUri();

        return ResponseEntity.created(location).body(product);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id) {
        return ResponseEntity.ok(productService.deleteProductById(id));
    }
    
}
