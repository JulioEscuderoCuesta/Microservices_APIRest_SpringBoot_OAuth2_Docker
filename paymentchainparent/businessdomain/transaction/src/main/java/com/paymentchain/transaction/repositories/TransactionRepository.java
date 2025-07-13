/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transaction.repositories;

import com.paymentchain.transaction.entities.Transaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
/**
 *
 * @author Hp
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    
    @Query("SELECT t FROM Transaction t WHERE t.reference = :reference")
    Optional<Transaction> findByReference(@Param ("reference") String reference);
    
    List<Transaction> findByIban(String iban);
}
