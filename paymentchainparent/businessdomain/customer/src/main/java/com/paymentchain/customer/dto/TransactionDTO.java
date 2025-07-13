/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
public class TransactionDTO {
    
    private String reference;
    private String iban;
    private LocalDateTime date;
    private Double amount;
    private Double fee;
    private String description;
    private String status;
    private String channel;
}
