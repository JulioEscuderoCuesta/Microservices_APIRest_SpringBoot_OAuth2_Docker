/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.services;

import com.paymentchain.customer.dto.ProductDTO;
import com.paymentchain.customer.exceptions.CustomerException;
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
public class ProductClientService {
    
    private final WebClient webClient;
        
    public ProductClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("http://BUSINESSDOMAIN-PRODUCT/product")
            .build();
    }
    
    public Mono<ProductDTO> getProduct(long id) {
        return webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(httpStatus -> httpStatus.is4xxClientError(), 
                error -> {
                    log.error("Client error getting product: {}", error);
                    return Mono.error(new CustomerException(
                            "PRODUCT NOT FOUND",
                            "Product not found",
                            HttpStatus.valueOf(error.statusCode().value())));
                    })
            .onStatus(httpStatus -> httpStatus.is5xxServerError(),
                error -> {
                    log.error("Server error getting product: {}", error);
                    return Mono.error(new CustomerException(
                        "PRODUCT_CLIENT_SERVICE_UNAVAILABLE",
                        "Product Client Service is temporarily unavailable",
                        HttpStatus.valueOf(error.statusCode().value())));
                })
            .bodyToMono(ProductDTO.class);
    }
    
}
