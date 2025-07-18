/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.billing.controllers;

import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.billing.respository.InvoiceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.http.HttpStatus;

/**
 *
 * @author sotobotero
 */
@Tag(name = "Billing API", description = "This API serve all funcionality for management Invoices") 
@RestController
@RequestMapping("/billing")
public class InvoiceRestController {
    
    @Autowired
    InvoiceRepository billingRepository;
    
    @Operation(summary = "Get all invoices", description = "Return a list of all invoices in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of invoices retrieved successfully", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class))), 
        @ApiResponse(responseCode = "204", description = "No invoices found"),
        @ApiResponse(responseCode = "500", description = "Internal error")})
    @GetMapping()
    public List<InvoiceResponse> list() {
        return billingRepository.findAll();
    }
    
    @Operation(summary = "Get invoice by ID",description = "Retrieves a specific invoice by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice found successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class))),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{id}")
    public ResponseEntity<?>  get(@PathVariable long id) {
          Optional<Invoice> invoice = billingRepository.findById(id);
        if (invoice.isPresent()) {
            return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "Update invoice", description = "Updates an existing invoice with new information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice updated successfully",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Invoice.class))),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody InvoiceRequest input) {
        return null;
    }
    
    @Operation(summary = "Create new invoice", description = "Creates a new invoice in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice created successfully",
            content = @Content( mediaType = "application/json", schema = @Schema(implementation = Invoice.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse( responseCode = "500", description = "Internal server error")})
    @PostMapping
    public ResponseEntity<?> post(@RequestBody InvoiceRequest input) {
        Invoice save = billingRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @Operation( summary = "Delete invoice", description = "Deletes an invoice from the system by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
         Optional<Invoice> dto = billingRepository.findById(Long.valueOf(id));
        if (!dto.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        billingRepository.delete(dto.get());
        return ResponseEntity.ok().build();
    }
    
}
