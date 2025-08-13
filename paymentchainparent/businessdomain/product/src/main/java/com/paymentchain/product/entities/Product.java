/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
@Entity
@Table(name = "products")
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;
    @NotBlank(message = "Product code is required")
    @Column(nullable = false, unique = true)
    private String code;
    @NotBlank(message = "Product price must be greater than 0")
    private Double price;
    
    
}
