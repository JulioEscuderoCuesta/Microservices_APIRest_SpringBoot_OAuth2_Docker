/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.exceptions;

import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.UnknownHostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 *
 * @author Hp
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ProblemDetail> handleTransactionException(TransactionException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            ex.getHttpStatus(), 
            ex.getMessage()
        );
        problemDetail.setType(URI.create("/errors/business/transaction"));
        problemDetail.setTitle("Transaction operation error");
        problemDetail.setProperty("code", ex.getCode());
        
        if (ex.getReference() != null) {
            problemDetail.setProperty("reference", ex.getReference());
        }
        
        return ResponseEntity.status(ex.getHttpStatus()).body(problemDetail);
    }
    
    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<?> handleUnkownHostException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                ex.getMessage());
        
        problemDetail.setType(URI.create("/errors/network/unknown-host"));
        problemDetail.setTitle("Network connectivity error");
        problemDetail.setProperty("code", "1024");
        problemDetail.setProperty("category", "TECNICO");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            ex.getMessage()
        );
        problemDetail.setType(URI.create("/errors/business/entity-not-found"));
        problemDetail.setTitle("Resource not found");
        problemDetail.setProperty("code", "404");
        problemDetail.setProperty("category", "NEGOCIO");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException ex) {
        // Evitar interferir con Swagger
        String packageName = ex.getClass().getPackage().getName();
        if (packageName.contains("swagger") || 
            packageName.contains("springdoc") || 
            packageName.contains("openapi")) {
            throw ex;
        }
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            ex.getMessage()
        );
        problemDetail.setType(URI.create("/errors/technical/runtime"));
        problemDetail.setTitle("Internal server error");
        problemDetail.setProperty("code", "500");
        problemDetail.setProperty("category", "TECNICO");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
    
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ProblemDetail> handleWebClientException(WebClientResponseException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.SERVICE_UNAVAILABLE,
            "External service is unavailable: " + ex.getMessage()
        );
        problemDetail.setType(URI.create("/errors/external-service/unavailable"));
        problemDetail.setTitle("Service unavailable");
        problemDetail.setProperty("originalStatus", ex.getStatusCode().value());
        problemDetail.setProperty("service", "customer-service");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problemDetail);
    }
}
