/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.exceptions;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Hp
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ProblemDetail> handleProductException(ProductException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(), 
                ex.getMessage());
        
        problemDetail.setType(URI.create("/errors/business/product"));
        problemDetail.setTitle("Product operation error");
        problemDetail.setProperty("code", ex.getCode());
        if (ex.getProductId() != null) {
            problemDetail.setProperty("productId", ex.getProductId());
        }   

        return ResponseEntity.status(ex.getHttpStatus()).body(problemDetail);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Validation failed: " + String.join(", ", errors)
        );
        problemDetail.setType(URI.create("/errors/validation/invalid-input"));
        problemDetail.setTitle("Validation error");
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
