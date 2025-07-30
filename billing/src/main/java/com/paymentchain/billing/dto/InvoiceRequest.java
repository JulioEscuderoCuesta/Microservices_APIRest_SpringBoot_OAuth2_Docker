/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.billing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
@Schema(name = "InvoiceRequest", description = "Invoice dto that represents billing information for customers in the request")
public class InvoiceRequest {

    @Schema(description = "Unique identifier of the customer who owns this invoice", example = "2")
    private Long customer;
    @Schema( description = "Invoice number as it appears on the physical invoice", example = "123", maxLength = 50)
    private String number;
    @Schema(description = "Detailed description of the invoice items or services", example = "Professional services consultation for Q1 2024", maxLength = 500)
    private String detail;
    @Schema(description = "Total amount of the invoice in the base currency", example = "1500.75", minimum = "0")
    private Double amount;
}
