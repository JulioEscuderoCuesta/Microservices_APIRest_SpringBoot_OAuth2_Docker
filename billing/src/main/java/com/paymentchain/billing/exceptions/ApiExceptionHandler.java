/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.billing.exceptions;

import java.net.URI;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Hp
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(InvoiceException.class)
    public ResponseEntity<ProblemDetail> handleCustomerException(InvoiceException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(), 
                ex.getMessage());
        
        problemDetail.setType(URI.create("/errors/business/customer"));
        problemDetail.setTitle("Customer operation error");
        problemDetail.setProperty("code", ex.getNumber());
        if (ex.getCustomerId() != null) {
            problemDetail.setProperty("customerId", ex.getCustomerId());
        }

        return ResponseEntity.status(ex.getHttpStatus()).body(problemDetail);
    }
}
