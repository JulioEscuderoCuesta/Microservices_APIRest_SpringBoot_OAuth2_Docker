/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
public class ProductDTO {
    
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "Product code is required")
    private String code;
    @NotBlank(message = "Product price is required")
    private Double price;
    
}
