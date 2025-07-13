/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
public class CustomerDTO {
    
    private long id;
    private String name;
    private Double balance;
    private String code;
    private String phone;
    private String iban;
    private String surname;
    private String address;
    private List<CustomerProductDTO> products;
    private List<TransactionDTO> transactions;
}
