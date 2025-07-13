/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionException extends RuntimeException {
    
    private final String code;
    private final HttpStatus httpStatus;
    private final String reference;
    
    public TransactionException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.reference = null;
    }
    
    public TransactionException(String reference, String code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.reference = reference;
    }
    
    // Getters
    public String getCode() { return code; }
    public HttpStatus getHttpStatus() { return httpStatus; }
    public String getReference() { return reference; }
}
