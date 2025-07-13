/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.paymentchain.transaction.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
public class UpdateTransactionDTO {
    
    @NotBlank(message = "Amount cannot be blank")
    private Double amount;
    @Positive(message = "Fee must not be negative, if present")
    private Double fee;
}
