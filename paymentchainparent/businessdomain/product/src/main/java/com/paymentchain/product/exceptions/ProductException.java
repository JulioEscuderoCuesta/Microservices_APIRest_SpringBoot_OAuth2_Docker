/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.exceptions;

import org.springframework.http.HttpStatus;

public class ProductException extends RuntimeException {
    
    private final String code;
    private final HttpStatus httpStatus;
    private final Long productId;
    
    public ProductException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.productId = null;
        this.httpStatus = httpStatus;
        this.code = code;
    }
    
    public ProductException(Long productId, String code, String message, HttpStatus httpStatus) {
        super(message);
        this.productId = productId;
        this.code = code;
        this.httpStatus = httpStatus;
    }
    
    // Getters
    public String getCode() { return code; }
    public HttpStatus getHttpStatus() { return httpStatus; }
    public Long getProductId() { return productId; }
}
