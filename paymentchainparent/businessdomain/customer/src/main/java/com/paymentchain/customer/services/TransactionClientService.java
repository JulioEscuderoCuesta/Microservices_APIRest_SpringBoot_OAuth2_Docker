/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.services;

import com.paymentchain.customer.dto.TransactionDTO;
import com.paymentchain.customer.exceptions.CustomerException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author Hp
 */
@Slf4j
@Service
public class TransactionClientService {
    
    private final WebClient webClient;

    public TransactionClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("http://BUSINESSDOMAIN-TRANSACTION/transaction")
            .build();
    }
    
    public List<TransactionDTO> getTransactions(String iban) {
        return webClient.get()
        .uri("/iban/{iban}", iban)
        .retrieve()
        .onStatus(httpStatus -> httpStatus.is4xxClientError(), 
            error -> {
                log.error("Client error getting transactions: {}", error);
                    return error.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Error response body: {}", errorBody);
                            return Mono.error(new CustomerException(
                                "IBAN_NOT_FOUND", 
                                "Iban not found: " + errorBody, 
                                HttpStatus.valueOf(error.statusCode().value())
                            ));
                        });                
            })
        .onStatus(httpStatus -> httpStatus.is5xxServerError(),
            error -> {
                log.error("Server error getting balance: {}", error);
                return Mono.error(new CustomerException(
                    "TRANSACTION_CLIENT_SERVICE_UNAVAILABLE", 
                    "Transaction client service is temporarily unavailable", 
                    HttpStatus.valueOf(error.statusCode().value())
                ));                
            })
        .bodyToFlux(TransactionDTO.class).collectList().block();
    }
}
