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
import com.paymentchain.billing.services.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author sotobotero
 */
@Tag(name = "Billing API", description = "This API serve all funcionality for management Invoices") 
@RestController
@RequestMapping("/billing")
public class InvoiceRestController {
    
    @Autowired
    InvoiceService invoiceService;
    
    @Operation(summary = "Get all invoices", description = "Return a list of all invoices in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of invoices retrieved successfully", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvoiceResponse.class))), 
        @ApiResponse(responseCode = "204", description = "No invoices found"),
        @ApiResponse(responseCode = "500", description = "Internal error")})
    @GetMapping()
    public ResponseEntity<List<InvoiceResponse>> list() {
        List<InvoiceResponse> invoiceResponses = invoiceService.findAll();
        if(invoiceResponses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(invoiceResponses);
    }
    
    @Operation(summary = "Get invoice by ID",description = "Retrieves a specific invoice by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice found successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Invoice.class))),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{id}")
    public ResponseEntity<?>  get(@PathVariable long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }
    
    @Operation(summary = "Update invoice", description = "Updates an existing invoice with new information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice updated successfully",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = Invoice.class))),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> put(@PathVariable Long id, @RequestBody InvoiceRequest invoiceRequest) {
        InvoiceResponse invoiceResponse = invoiceService.updateInvoice(id, invoiceRequest);
        return ResponseEntity.ok(invoiceResponse);
    }
    
    @Operation(summary = "Create new invoice", description = "Creates a new invoice in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice created successfully",
            content = @Content( mediaType = "application/json", schema = @Schema(implementation = Invoice.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse( responseCode = "500", description = "Internal server error")})
    @PostMapping
    public ResponseEntity<?> post(@RequestBody InvoiceRequest input) {
        InvoiceResponse invoiceResponse = invoiceService.createInvoice(input);
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(invoiceResponse.getId())
            .toUri();
        
        return ResponseEntity.created(location).body(invoiceResponse);
    }
    
    @Operation( summary = "Delete invoice", description = "Deletes an invoice from the system by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        InvoiceResponse invoiceResponse = invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(invoiceResponse);
    }
    
}
