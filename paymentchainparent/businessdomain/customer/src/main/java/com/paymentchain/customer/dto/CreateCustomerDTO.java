/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
public class CreateCustomerDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotBlank(message = "Code cannot be blank")
    private String code;
    
    @NotBlank(message = "Phone cannot be blank")
    private String phone;
    
    @NotBlank(message = "IBAN cannot be blank")
    private String iban;
    
    @NotBlank(message = "Surname cannot be blank")
    private String surname;
    
    @NotBlank(message = "Address cannot be blank")
    private String address;
}
