/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.service;

import com.paymentchain.transaction.dtos.CustomerDTO;
import com.paymentchain.transaction.exceptions.TransactionException;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author Hp
 */
@Slf4j
@Service
public class CustomerClientService {
    
    private final WebClient webClient;

    public CustomerClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("http://BUSINESSDOMAIN-CUSTOMER/customer")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
    
    public CustomerDTO getCustomerByIban(String iban) {
        try {
            log.info("Starting request to get customer with IBAN: {}", iban);
            String uri = "/iban/{iban}";
            log.info("Full request URL will be: {}{}", webClient.get().uri(uri, iban), uri);

            CustomerDTO customer = webClient.get()
                .uri(uri, iban)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError(), 
                    error -> {
                        log.error("Client error getting customer. Status code: {}", error.statusCode());
                        return error.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("Error response body: {}", errorBody);
                                return Mono.error(new TransactionException(
                                    "CUSTOMER_NOT_FOUND", 
                                    "Customer not found: " + errorBody, 
                                    HttpStatus.valueOf(error.statusCode().value())
                                ));
                            });
                    })
                .onStatus(httpStatus -> httpStatus.is5xxServerError(),
                    error -> {
                        log.error("Server error from customer service. Status code: {}", error.statusCode());
                        return Mono.error(new TransactionException(
                            "CUSTOMER_CLIENT_SERVICE_UNAVAILABLE", 
                            "Customer client service is temporarily unavailable", 
                            HttpStatus.valueOf(error.statusCode().value())
                        ));
                    })
                .bodyToMono(CustomerDTO.class)
                .block();

            log.info("Successfully retrieved customer: {}", customer);
            return customer;
            
        } catch (Exception e) {
            if (e.getCause() instanceof UnknownHostException) {
                throw new TransactionException(
                    "CUSTOMER_SERVICE_UNAVAILABLE", 
                    "Customer service is not available (network error)", 
                    HttpStatus.SERVICE_UNAVAILABLE
                );            }
            if (e instanceof TransactionException transactionException) {
                throw transactionException;
            }
            log.error("Unexpected error getting customer", e);
            throw new TransactionException(
                "CUSTOMER_SERVICE_ERROR", 
                "Failed to get customer: " + e.getMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    
    public CustomerDTO updateBalance(String iban, double amountOfTransaction) {
        try {
            log.info("Starting request to update balance for customer with IBAN: {} with amount: {}", iban, amountOfTransaction);
            String uri = "/iban/{iban}/balance";
            log.info("Full request URL will be: {}{}", webClient.mutate().build().put().uri(uri, iban), uri);

            CustomerDTO updatedCustomer = webClient.put()
                .uri(uri, iban)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(amountOfTransaction)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is4xxClientError(), 
                    error -> {
                        log.error("Client error updating customer balance. Status code: {}", error.statusCode());
                        return error.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                log.error("Error response body: {}", errorBody);
                                return Mono.error(new TransactionException(
                                    "CUSTOMER_NOT_FOUND", 
                                    "Customer not found for balance update: " + errorBody, 
                                    HttpStatus.valueOf(error.statusCode().value())
                                ));
                            });
                    })
                .onStatus(httpStatus -> httpStatus.is5xxServerError(),
                    error -> {
                        log.error("Server error from customer service during balance update. Status code: {}", error.statusCode());
                        return Mono.error(new TransactionException(
                            "CUSTOMER_CLIENT_SERVICE_UNAVAILABLE", 
                            "Customer client service is temporarily unavailable for balance update", 
                            HttpStatus.valueOf(error.statusCode().value())
                        ));
                    })
                .bodyToMono(CustomerDTO.class)
                .block();

            log.info("Successfully updated customer balance: {}", updatedCustomer);
            return updatedCustomer;

        } catch (Exception e) {
            if (e.getCause() instanceof UnknownHostException) {
                throw new TransactionException(
                    "CUSTOMER_SERVICE_UNAVAILABLE", 
                    "Customer service is not available (network error) during balance update", 
                    HttpStatus.SERVICE_UNAVAILABLE
                );
            }
            if (e instanceof TransactionException transactionException) {
                throw transactionException;
            }
            log.error("Unexpected error updating customer balance", e);
            throw new TransactionException(
                "CUSTOMER_SERVICE_ERROR", 
                "Failed to update customer balance: " + e.getMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
