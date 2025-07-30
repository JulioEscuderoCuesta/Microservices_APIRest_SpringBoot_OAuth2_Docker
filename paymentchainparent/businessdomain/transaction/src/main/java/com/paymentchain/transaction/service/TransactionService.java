/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.service;

import com.paymentchain.transaction.dtos.CreateTransactionDTO;
import com.paymentchain.transaction.dtos.TransactionDTO;
import com.paymentchain.transaction.entities.Status;
import com.paymentchain.transaction.entities.Transaction;
import com.paymentchain.transaction.exceptions.TransactionException;
import com.paymentchain.transaction.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hp
 */
@Service
public class TransactionService {
 
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CustomerClientService customerClientService;
       
    private static final double WITHDRAWAL_FEE = 0.98;
    
    public List<TransactionDTO> findAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        
        return transactions.stream()
            .map(transaction -> {
                return convertToDTO(transaction);
            })
            .collect(Collectors.toList());
    }
    
    public TransactionDTO getTransactionByReference(String reference) {
        Transaction transaction = transactionRepository.findByReference(reference)
            .orElseThrow(() -> new TransactionException(
                reference,
                "TRANSACTION_NOT_FOUND", 
                "Transaction with reference " + reference + " does not exist", 
                HttpStatus.NOT_FOUND
            ));
        return convertToDTO(transaction);
    } 
    
    public List<TransactionDTO> getTransactionsByIban(String iban) {
        List<Transaction> transactions = transactionRepository.findByIban(iban);
        return transactions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO createTransaction(CreateTransactionDTO transactionDTO) {  
        String iban = transactionDTO.getIban();
        Transaction transaction = new Transaction();

        double amount = transactionDTO.getAmount();
        double fee;
        double realAmount;
        
        // If it's a withdrawal, the fee of 0.98% is applied
        if (amount < 0)
        {
            fee = Math.abs(amount * WITHDRAWAL_FEE / 100);
            realAmount = amount + fee;
        }
        else
        {
            fee = transactionDTO.getFee();
            realAmount = amount - fee;
        }
        
        transaction.setReference(generateReference());
        transaction.setIban(iban);
        transaction.setChannel(transactionDTO.getChannel());
        transaction.setDescription(!transactionDTO.getDescription().isBlank() ? transactionDTO.getDescription() : "");
        transaction.setDate(transactionDTO.getDate());
        transaction.setFee(fee);
        transaction.setAmount(transactionDTO.getAmount());
        setTransactionStatus(transaction);

        customerClientService.updateBalance(iban, realAmount);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }
        
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setReference(transaction.getReference());
        dto.setIban(transaction.getIban());
        dto.setDate(transaction.getDate());
        dto.setAmount(transaction.getAmount());
        dto.setFee(transaction.getFee());
        dto.setDescription(transaction.getDescription());
        dto.setStatus(transaction.getStatus());
        dto.setChannel(transaction.getChannel());
        
        return dto;
    } 
    
    private String generateReference() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = String.format("%d%02d%02d%02d%02d%02d",
            now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
            now.getHour(), now.getMinute(), now.getSecond());
        String random = String.format("%04d", new Random().nextInt(10000));
        
        return timestamp + "-" + random;
    }
    
    private void setTransactionStatus(Transaction transaction) {
        Status status = transaction.getDate().isAfter(LocalDateTime.now()) 
            ? Status.PENDIENTE 
            : Status.LIQUIDADA;
        transaction.setStatus(status);      
    }
}
