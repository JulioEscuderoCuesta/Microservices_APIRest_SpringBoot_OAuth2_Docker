/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
@Entity
public class Customer {
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @NotBlank(message = "IBAN cannot be blank")
    private long id;
    @NotBlank(message = "IBAN cannot be blank")
    private String name;
    @NotBlank(message = "IBAN cannot be blank")
    private double balance;
    @NotBlank(message = "IBAN cannot be blank")
    private String code;
    @NotBlank(message = "IBAN cannot be blank")
    private String phone;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "IBAN cannot be blank")
    private String iban;
    @NotBlank(message = "IBAN cannot be blank")
    private String surname;
    @NotBlank(message = "IBAN cannot be blank")
    private String address;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProduct> products;
    @Transient
    private List<?> transactions;
  
}
