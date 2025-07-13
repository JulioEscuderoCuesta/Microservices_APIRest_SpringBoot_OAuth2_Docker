/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Entity
@Data
public class Transaction {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @NotBlank(message = "Reference cannot be blank")
    @Column(unique = true, nullable = false)
    private String reference;
    @NotBlank(message = "Iban cannot be blank")
    @Column(nullable = false)
    private String iban;
    @NotNull(message = "Date cannot be null")
    @Column(nullable = false)
    private LocalDateTime date;
    @NotNull(message = "Amount cannot be null")
    @Column(nullable = false)
    private Double amount;
    @PositiveOrZero(message = "Fee cannot be negative")
    @Column(nullable = false)
    private Double fee;
    private String description;
    @NotNull(message = "Status cannot be blank")
    private Status status;
    @NotNull(message = "Channel cannot be null")
    private Channel channel;
}
