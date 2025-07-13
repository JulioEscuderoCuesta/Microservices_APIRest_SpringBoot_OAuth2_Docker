/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.dtos;

import com.paymentchain.transaction.entities.Channel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
public class CreateTransactionDTO {
    
    @NotBlank(message = "Iban cannot be blank")
    private String iban;
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;
    @NotNull(message = "Amount cannot be blank")
    private Double amount;
    @PositiveOrZero(message = "Fee cannot be negative")
    private Double fee;
    private String description;
    @NotNull(message = "Channel cannot be null")
    private Channel channel;
}
