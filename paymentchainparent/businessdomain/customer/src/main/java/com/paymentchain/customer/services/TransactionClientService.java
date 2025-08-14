/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.services;

import com.paymentchain.customer.dto.TransactionDTO;
import com.paymentchain.customer.exceptions.CustomerException;
import io.netty.handler.timeout.TimeoutException;
import java.net.ConnectException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
            .build();
    }

    public Optional<List<TransactionDTO>> getCustomerTransactionsByIban(String iban) {
        try {
            List<TransactionDTO> transactions = getTransactionsSafely(iban);
            return Optional.of(transactions);
        } catch (Exception e) {
            log.warn("Transaction service unavailable for IBAN {}: {}", iban, e.getMessage());
            return Optional.empty();
        }
    }
    
    public List<TransactionDTO> getTransactionsSafely(String iban) {
        return webClient.get()
        .uri("/iban/{iban}", iban)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, 
            response  -> {
                if (response.statusCode().value() == 404) {
                    log.debug("No transactions found for IBAN: {}", iban);
                    return Mono.empty();
                } else {
                    log.error("Client error getting transactions for IBAN {}: {}", iban, response.statusCode());
                    return response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Error response body: {}", errorBody);
                            return Mono.error(new CustomerException(
                                "IBAN_NOT_FOUND", 
                                "Iban not found: " + errorBody, 
                                HttpStatus.valueOf(response.statusCode().value())
                            ));
                        });
                }
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
        .bodyToFlux(TransactionDTO.class)
        .collectList()
        .timeout(Duration.ofSeconds(5))
        .onErrorReturn(TimeoutException.class, Collections.emptyList())
        .onErrorReturn(ConnectException.class, Collections.emptyList())
        .block();
    }
    
    
}
