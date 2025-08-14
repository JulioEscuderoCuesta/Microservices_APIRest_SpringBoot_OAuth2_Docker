/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.exceptions;

import org.springframework.http.HttpStatus;

/**
 *
 * @author Hp
 */
public class CustomerException extends RuntimeException {

    private final Long customerId;
    private final String code;
    private final HttpStatus httpStatus;
    private final String iban;

    public CustomerException(Long customerId, String code, String message, HttpStatus httpStatus) {
        super(message);
        this.customerId = customerId;
        this.iban = null;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public CustomerException(String iban, String code, String message, HttpStatus httpStatus) {
        super(message);
        this.customerId = null;
        this.iban = iban;
        this.code = code;
        this.httpStatus = httpStatus;
    }
    
    public CustomerException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.customerId = null;
        this.iban = null;
        this.code = code;
        this.httpStatus = httpStatus;
    }
    
    public Long getCustomerId() {
        return customerId;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
    public String getIban() {
        return iban;
    }
}
