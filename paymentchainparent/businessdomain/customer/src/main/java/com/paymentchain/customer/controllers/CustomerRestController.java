/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.customer.controllers;

import com.paymentchain.customer.dto.CreateCustomerDTO;
import com.paymentchain.customer.dto.CustomerBalanceDTO;
import com.paymentchain.customer.dto.CustomerDTO;
import com.paymentchain.customer.dto.TransactionDTO;
import com.paymentchain.customer.dto.UpdateCustomerDTO;
import com.paymentchain.customer.services.CustomerService;
import java.net.URI;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.env.Environment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Hp
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {
    
    @Autowired
    CustomerService customerService;
    @Autowired
    private Environment env;

    //Http Methods
    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> getAll() {
        List<CustomerDTO> customers = customerService.getCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);        
    }
    
    @GetMapping("/check")
    public String check() {
        return "Hello your property value is: " + env.getProperty("custom.activeprofileName");
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
    
    @GetMapping("/iban/{iban}")
    public ResponseEntity<CustomerBalanceDTO> getCustomerByIban(@PathVariable String iban) {
        CustomerDTO customerDTO = customerService.getCustomerByIban(iban);
        CustomerBalanceDTO balanceDTO = new CustomerBalanceDTO();
        balanceDTO.setBalance(customerDTO.getBalance());
        balanceDTO.setIban(customerDTO.getIban());
        return ResponseEntity.ok(balanceDTO);
    }
    
    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getCustomerBalance(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerBalance(id));
        
    }
    
    @GetMapping("/full")
    public ResponseEntity<CustomerDTO> getByCode(@RequestParam String code) {
        CustomerDTO customer = customerService.getCustomerByCode(code);
        return ResponseEntity.ok(customer);
    }
    
    @GetMapping("/{iban}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable String iban) {
        return ResponseEntity.ok(customerService.getTransactionsByIban(iban));  
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> put(@PathVariable Long id, @RequestBody UpdateCustomerDTO updateDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, updateDTO);
        return ResponseEntity.ok(updatedCustomer);
    }
    
    @PutMapping("/iban/{iban}/balance")
    public ResponseEntity<CustomerDTO> updateBalance(@PathVariable String iban, @RequestBody double amount) {
        CustomerDTO updatedCustomer = customerService.updateBalance(iban, amount);
        return ResponseEntity.ok(updatedCustomer);
    }
        
    @PostMapping
    public ResponseEntity<CustomerDTO> post(@RequestBody CreateCustomerDTO createCustomerDTO) {
        CustomerDTO postCustomer = customerService.createCustomer(createCustomerDTO);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(postCustomer.getId())
            .toUri();

        return ResponseEntity.created(location).body(postCustomer);
    }
    
    @PostMapping("/{customerId}/products/{productId}")
    public ResponseEntity<CustomerDTO> addProduct(@PathVariable Long customerId, @PathVariable Long productId) {
        CustomerDTO customer = customerService.addProductToCustomer(customerId, productId);

        URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/customers/{id}")
            .buildAndExpand(customerId)
            .toUri();

        return ResponseEntity.created(location).body(customer);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDTO> delete(@PathVariable Long id) {
        CustomerDTO customer = customerService.deleteCustomer(id);
        return ResponseEntity.ok(customer);
    }
    
    @DeleteMapping("/{customerId}/products/{productId}")
    public ResponseEntity<CustomerDTO> removeProduct(@PathVariable Long customerId, @PathVariable Long productId) {
        CustomerDTO customer = customerService.removeProductFromCustomer(customerId, productId);
        return ResponseEntity.ok(customer);
    }
}
