/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.dto;

import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
public class UpdateCustomerDTO {
    private String name;
    private String code;
    private String phone;
    private String iban;
    private String surname;
    private String address;
}
