/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.paymentchain.customer.repositories;

import com.paymentchain.customer.entities.Customer;
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
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.products")
    List<Customer> findCustomers();
    
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.products WHERE c.id = :id")
    public Optional<Customer> findByIdWithProducts(@Param ("id") Long id);
        
    @Query("SELECT c.balance FROM Customer c WHERE c.id = :id")
    public Optional<Double> findCustomerBalance(@Param ("id") Long id);
    
    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.products WHERE c.code = :code")
    public Optional<Customer> findByCodeWithProducts(@Param ("code") String code);
    
    @Query("SELECT c FROM Customer c WHERE c.iban = :iban")
    public Optional<Customer> findByIban(@Param ("iban") String iban);
    
}
