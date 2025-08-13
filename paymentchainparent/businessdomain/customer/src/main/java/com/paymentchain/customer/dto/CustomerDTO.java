/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.dto;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Hp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    
    private long id;
    private String name;
    private Double balance;
    private String code;
    private String phone;
    private String iban;
    private String surname;
    private String address;
    // Lists of products and transactions with default values
    private List<CustomerProductDTO> products = Collections.emptyList();
    private List<TransactionDTO> transactions = Collections.emptyList();
    // Availability flags
    private boolean productsAvailable = true;
    private boolean transactionsAvailable = true;
    // Error messages
    private String productsError;
    private String transactionsError;    
    
    public boolean hasProductsError() {
        return !productsAvailable;
    }
    
    public boolean hasTransactionsError() {
        return !transactionsAvailable;
    }
    
    public boolean hasAnyError() {
        return hasProductsError() || hasTransactionsError();
    }

}
