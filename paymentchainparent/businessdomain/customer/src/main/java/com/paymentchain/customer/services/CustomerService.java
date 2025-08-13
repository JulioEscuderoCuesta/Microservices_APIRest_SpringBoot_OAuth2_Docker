/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.services;

import com.paymentchain.customer.dto.CreateCustomerDTO;
import com.paymentchain.customer.dto.CustomerDTO;
import com.paymentchain.customer.dto.CustomerProductDTO;
import com.paymentchain.customer.dto.ProductDTO;
import com.paymentchain.customer.dto.TransactionDTO;
import com.paymentchain.customer.dto.UpdateCustomerDTO;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exceptions.CustomerException;
import com.paymentchain.customer.repositories.CustomerRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 * @author Hp
 */
@Slf4j
@Service 
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final ProductClientService productClientService;
    private final TransactionClientService transactionClientService;

    public CustomerService(
            CustomerRepository customerRepository, 
            ProductClientService productClientService,
            TransactionClientService transactionClientService) {
        this.customerRepository = customerRepository;
        this.productClientService = productClientService;
        this.transactionClientService = transactionClientService;
    }
    
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomers() {
        List<Customer> customers = customerRepository.findCustomers();

        return customers.stream()
                .map(this::convertToDTOWithExternalServices)
                .collect(Collectors.toList());
    }
    
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdWithProducts(id)
            .orElseThrow(() -> new CustomerException(
                id,
                "CUSTOMER_NOT_FOUND", 
                "Customer with id " + id + " does not exist", 
                HttpStatus.NOT_FOUND
            ));
        return convertToDTOWithExternalServices(customer);
    }
    
    public Double getCustomerBalance(Long id) {
        return customerRepository.findCustomerBalance(id)
            .orElseThrow(() -> new CustomerException(
                    id,
                    "CUSTOMER_NOT_FOUND",
                    "Customer with id " + id + " does not exist",
                    HttpStatus.NOT_FOUND
            ));
    }
    
    public CustomerDTO getCustomerByCode(String code) {
        Customer customer = customerRepository.findByCodeWithProducts(code)
            .orElseThrow(() -> new CustomerException(
                "CUSTOMER_NOT_FOUND_BY_CODE", 
                "Customer with code " + code + " does not exist", 
                HttpStatus.NOT_FOUND
            ));
        return convertToDTOWithExternalServices(customer);
    }
    
    public CustomerDTO getCustomerByIban(String iban) {
        Customer customer = customerRepository.findByIban(iban)
            .orElseThrow(() -> new CustomerException(
                "CUSTOMER_NOT_FOUND_BY_IBAN", 
                "Customer with IBAN " + iban + " does not exist", 
                HttpStatus.NOT_FOUND
            ));
        return convertToDTOWithExternalServices(customer);
    }
    
    public List<TransactionDTO> getCustomerTransactionsByIban(String iban) {
        Optional<List<TransactionDTO>> transactionsOpt = 
            transactionClientService.getCustomerTransactionsByIban(iban);
        
        if (transactionsOpt.isPresent()) {
            return transactionsOpt.get();
        } else {
            log.warn("Transaction service unavailable for IBAN: {}", iban);
            return Collections.emptyList();
        }
    }


    public CustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        Customer customer = new Customer();
        customer.setName(createCustomerDTO.getName());
        customer.setBalance(0.0);
        customer.setCode(createCustomerDTO.getCode());
        customer.setPhone(createCustomerDTO.getPhone());
        customer.setIban(createCustomerDTO.getIban());
        customer.setSurname(createCustomerDTO.getSurname());
        customer.setAddress(createCustomerDTO.getAddress());
        
        try {
            Customer savedCustomer = customerRepository.save(customer);
            return convertToDTOBasic(savedCustomer);

        } catch (DataIntegrityViolationException  e) {
            throw new CustomerException(
                "ATTRIBUTE_ALREADY_EXISTS",
                "A customer with the same attribute already exists",
                HttpStatus.CONFLICT
            );        
        } catch (ConstraintViolationException e) {
            log.error("Constraint violation creating customer: {}", e.getMessage());
            throw new CustomerException(
                "INVALID_CUSTOMER_DATA",
                "Customer data is invalid: " + e.getMessage(),
                HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            log.error("Unexpected error creating customer", e);
            throw new CustomerException(
                "CUSTOMER_CREATION_ERROR",
                "Failed to create customer: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        
    }
    
    public CustomerDTO deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerException(
                id,
                "CUSTOMER_NOT_FOUND", 
                "Customer with id " + id + " does not exist", 
                HttpStatus.NOT_FOUND
            ));

        customerRepository.deleteById(id);
        return convertToDTOBasic(customer);
    }
    
    public CustomerDTO updateCustomer(Long id, UpdateCustomerDTO updateDTO) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerException(
                id, 
                "CUSTOMER_NOT_FOUND", 
                "Customer with id " + id + " does not exist", 
                HttpStatus.NOT_FOUND
            ));        

        if (updateDTO.getName() != null) customer.setName(updateDTO.getName());
        if (updateDTO.getCode() != null) customer.setCode(updateDTO.getCode());
        if (updateDTO.getPhone() != null) customer.setPhone(updateDTO.getPhone());
        if (updateDTO.getIban() != null) customer.setIban(updateDTO.getIban());
        if (updateDTO.getSurname() != null) customer.setSurname(updateDTO.getSurname());
        if (updateDTO.getAddress() != null) customer.setAddress(updateDTO.getAddress());

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTOWithExternalServices(updatedCustomer);
    }
    
    public CustomerDTO updateBalance(String iban, double amount) {
        Customer customer = customerRepository.findByIban(iban)
            .orElseThrow(() -> new CustomerException(
                "CUSTOMER_NOT_FOUND_BY_IBAN", 
                "Customer with IBAN " + iban + " does not exist", 
                HttpStatus.NOT_FOUND
            ));
        
        double finalBalance = customer.getBalance() + amount;
        if (finalBalance <= 0)
            throw new CustomerException(
                customer.getId(), 
                "INSUFFICIENT_BALANCE", 
                String.format("Insufficient balance. Current: %.2f, Transaction: %.2f, Final would be: %.2f", 
                    customer.getBalance(), amount, finalBalance),
                HttpStatus.FORBIDDEN
            );  
        else {
            customer.setBalance(finalBalance);
            Customer savedCustomer = customerRepository.save(customer);
            return convertToDTOBasic(savedCustomer);
        }
    }
    
    public CustomerDTO addProductToCustomer(Long customerId, Long productId) {
        Customer customer = findCustomerById(customerId);
        ValidateProduct(productId);

        boolean productAlreadyAssociated = customer.getProducts().stream()
        .anyMatch(p -> p.getProductId() == productId);
        
        if (productAlreadyAssociated) {
            throw new CustomerException(
                customerId,
                "PRODUCT_ALREADY_ASSOCIATED", 
                "Product " + productId + " is already associated with customer " + customerId, 
                HttpStatus.CONFLICT
            );        
        }

        CustomerProduct customerProduct = new CustomerProduct();
        customerProduct.setProductId(productId);
        customerProduct.setCustomer(customer);
        customer.getProducts().add(customerProduct); 

        Customer savedCustomer = customerRepository.save(customer);

        return convertToDTOWithExternalServices(savedCustomer);
        
    }

    public CustomerDTO removeProductFromCustomer(Long customerId, Long productId) {
        Customer customer = findCustomerById(customerId);
        ValidateProduct(productId);

        boolean productRemoved = customer.getProducts()
            .removeIf(p -> p.getProductId() == productId);

        if (!productRemoved) {
            throw new CustomerException(
                customerId,
                "PRODUCT_NOT_ASSOCIATED", 
                "Product " + productId + " is not associated with customer " + customerId, 
                HttpStatus.NOT_FOUND
            );  
        }
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTOWithExternalServices(savedCustomer);
  
    }
    
    private CustomerDTO convertToDTOBasic(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setBalance(customer.getBalance());
        dto.setCode(customer.getCode());
        dto.setPhone(customer.getPhone());
        dto.setIban(customer.getIban());
        dto.setSurname(customer.getSurname());
        dto.setAddress(customer.getAddress());
        
        return dto;
    }
    
    private CustomerDTO convertToDTOWithExternalServices(Customer customer) {
        CustomerDTO customerDTO = convertToDTOBasic(customer);
        
        // Obtain products if possible
        enrichWithProducts(customerDTO, customer);
        // Obtain transactions if possible
        enrichWithTransactions(customerDTO, customer);
        
        return customerDTO;
    }
    
    private void enrichWithProducts(CustomerDTO dto, Customer customer) {
        try {
            List<CustomerProductDTO> products = mapCustomerProducts(customer);
            dto.setProducts(products);
            dto.setProductsAvailable(true);
        } catch (Exception e) {
            log.warn("Product service unavailable for customer {}: {}", customer.getId(), e.getMessage());
            dto.setProducts(Collections.emptyList());
            dto.setProductsAvailable(false);
            dto.setProductsError("Product service temporarily unavailable");
        }
    }
    
    private void enrichWithTransactions(CustomerDTO dto, Customer customer) {
        try {
            Optional<List<TransactionDTO>> transactionsOpt = 
            transactionClientService.getCustomerTransactionsByIban(customer.getIban());
        
            dto.setTransactionsAvailable(true);

            if (transactionsOpt.isPresent()) {
                dto.setTransactions(transactionsOpt.get());
            } else {
                dto.setTransactions(Collections.emptyList());
            }
            
        } catch (Exception e) {
            log.warn("Unexpected error getting transactions for customer {}: {}", customer.getId(), e.getMessage());
            dto.setTransactions(Collections.emptyList());
            dto.setTransactionsAvailable(false);
            dto.setTransactionsError("Transaction service temporarily unavailable");
        }

    }
    
    private Customer findCustomerById(Long customerId) {
        return customerRepository.findByIdWithProducts(customerId)
            .orElseThrow(() -> new CustomerException(
                customerId,
                "CUSTOMER_NOT_FOUND", 
                "Customer with id " + customerId + " does not exist", 
                HttpStatus.NOT_FOUND
            ));    
    }
    
    private List<CustomerProductDTO> mapCustomerProducts(Customer customer) {
        List<Long> productIds = customer.getProducts().stream()
            .map(CustomerProduct::getProductId)
            .collect(Collectors.toList());
        
        if (productIds.isEmpty()) 
            return testProductServiceAvailability();
        
        Map<Long, ProductDTO> productInfoMap = getProductsBatch(productIds);
        
        return customer.getProducts().stream()
            .map(product -> {
                CustomerProductDTO productDTO = new CustomerProductDTO();
                productDTO.setId(product.getId());
                productDTO.setProductId(product.getProductId());
                
                ProductDTO productInfo = productInfoMap.get(product.getProductId());
                if (productInfo != null) {
                    productDTO.setProductName(productInfo.getName());
                } else {
                    productDTO.setProductName("Product info unavailable");
                    log.warn("Product info not found for productId: {}", product.getProductId());
                }
                
                return productDTO;
            })
            .collect(Collectors.toList());

    }
    
    private Map<Long, ProductDTO> getProductsBatch(List<Long> productIds) {
        try {
            List<ProductDTO> products = productClientService.getProductsByIds(productIds);
            return products.stream()
                .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));
                
        } catch (Exception e) {
            log.warn("Batch product service call failed, falling back to individual calls");
            
            return productIds.stream()
                .collect(Collectors.toMap(
                    Function.identity(),
                    this::getProductSafely,
                    (existing, replacement) -> existing
                ));
        }
    }
    
    private ProductDTO getProductSafely(Long productId) {
        try {
            return productClientService.getProduct(productId)
                .timeout(Duration.ofSeconds(2))
                .block();
        } catch (Exception e) {
            log.warn("Failed to get product {}: {}", productId, e.getMessage());
            // Retorna un producto "dummy" en lugar de null
            ProductDTO dummy = new ProductDTO();
            dummy.setId(productId);
            dummy.setName("Product unavailable");
            return dummy;
        }
    }
    
    private void ValidateProduct(Long productId) {
        ProductDTO productInfo = productClientService.getProduct(productId).block();    
    }

    private List<CustomerProductDTO> testProductServiceAvailability() {
        try {
            productClientService.healthCheck();
            log.debug("Product service is available, customer genuinely has no products");
            return Collections.emptyList();

        } catch (Exception e) {
            log.warn("Product service connectivity test failed: {}", e.getMessage());
            throw new RuntimeException("Product service connectivity test failed", e);
        }
    }
}
