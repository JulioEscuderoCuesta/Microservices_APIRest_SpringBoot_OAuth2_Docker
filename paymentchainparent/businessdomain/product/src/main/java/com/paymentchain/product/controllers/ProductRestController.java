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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Hp
 */
@RestController
@RequestMapping("/product")
public class ProductRestController {
    
    @Autowired
    ProductService productService;
        
    @GetMapping()
    public List<Product> findAll() {
        return productService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }
    
    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody ProductDTO productDTO) {
        System.out.println("=== DEBUGGING ===");
        System.out.println("Name: '" + productDTO.getName() + "'");
        System.out.println("Code: '" + productDTO.getCode() + "'");
        System.out.println("Name is blank: " + (productDTO.getName() == null || productDTO.getName().trim().isEmpty()));
        System.out.println("Code is blank: " + (productDTO.getCode() == null || productDTO.getCode().trim().isEmpty()));
        Product product = productService.createProduct(productDTO);

        URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/product/{id}")
            .buildAndExpand(product.getId())
            .toUri();

        return ResponseEntity.created(location).body(product);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(productService.deleteProductById(id));
    }
    
}
