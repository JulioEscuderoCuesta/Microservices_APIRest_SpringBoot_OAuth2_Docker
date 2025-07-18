/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.billing.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 * @author Julio Escudero Cuesta
 */
@Entity
@Data
@Schema(name = "Invoice", description = "Invoice entity that represents billing information for customers")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique identifier for the invoice", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;
    @Schema(description = "Unique identifier of the customer who owns this invoice", example = "2")
    private long customerId;
    @Schema( description = "Invoice number as it appears on the physical invoice", example = "123", maxLength = 50)
    private String number;
    @Schema(description = "Detailed description of the invoice items or services", example = "Professional services consultation for Q1 2024", maxLength = 500)
    private String detail;
    @Schema(description = "Total amount of the invoice in the base currency", example = "1500.75", minimum = "0")
    private double amount;
}
