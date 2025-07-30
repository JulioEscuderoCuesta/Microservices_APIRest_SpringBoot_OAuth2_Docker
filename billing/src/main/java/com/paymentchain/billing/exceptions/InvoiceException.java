/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.billing.exceptions;

import org.springframework.http.HttpStatus;

/**
 *
 * @author Hp
 */
public class InvoiceException extends RuntimeException {
    
    private final Long customerId;
    private final String number;
    private final HttpStatus httpStatus;
    
    public InvoiceException(Long customerId, String number, String message, HttpStatus httpStatus) {
       super(message);
       this.customerId = customerId;
       this.number = number;
       this.httpStatus = httpStatus;
   }  
    
    public InvoiceException(String code, String message,HttpStatus httpStatus) {
       super(message);
       this.customerId = null;
       this.number = "";
       this.httpStatus = httpStatus;
   }  

   public Long getCustomerId() { return customerId; }
   public String getNumber() { return number; }
   public HttpStatus getHttpStatus() { return httpStatus; }
    
}
