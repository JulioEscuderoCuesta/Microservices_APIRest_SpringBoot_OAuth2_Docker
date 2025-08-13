/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.controllers;

import com.paymentchain.transaction.dtos.CreateTransactionDTO;
import com.paymentchain.transaction.dtos.TransactionDTO;
import com.paymentchain.transaction.service.TransactionService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Hp
 */
@Slf4j
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    
    @Autowired
    TransactionService transactionService;

    @GetMapping()
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.findAll();
    }
    
    @GetMapping("/reference/{reference}")
    public ResponseEntity<TransactionDTO> getTransactionByReference(@PathVariable String reference) {
        return ResponseEntity.ok(transactionService.getTransactionByReference(reference));
    }
    
    @GetMapping("/iban/{iban}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByIban(@PathVariable String iban) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByIban(iban);
        if (transactions.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(transactions);  
    }
    
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody CreateTransactionDTO createTransactionDTO) {
        TransactionDTO transactionDTO = transactionService.createTransaction(createTransactionDTO);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/reference/{reference}")
            .buildAndExpand(transactionDTO.getReference())
            .toUri();

        return ResponseEntity.created(location).body(transactionDTO);
    }
}
