/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.billing.services;

import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import com.paymentchain.billing.exceptions.InvoiceException;
import com.paymentchain.billing.mappers.InvoiceRequestMapper;
import com.paymentchain.billing.mappers.InvoiceResponseMapper;
import com.paymentchain.billing.repositories.InvoiceRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hp
 */
@Service
public class InvoiceService {
    
    @Autowired
    InvoiceRepository invoiceRepository;
    
    @Autowired
    InvoiceRequestMapper invoiceRequestMapper;
    @Autowired
    InvoiceResponseMapper invoiceResponseMapper;
    
    public List<InvoiceResponse> findAll() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoiceResponseMapper.InvoicetListToInvoiceResponseList(invoices);
    }
    
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new InvoiceException(
                "INVOICE_NOT_FOUND_BY_ID",
                "Invoice with id " + id + "does not exist",
                HttpStatus.NOT_FOUND
            ));
        
        return invoiceResponseMapper.InvoiceToInvoiceResponse(invoice);
    }
    
    public InvoiceResponse updateInvoice(Long id, InvoiceRequest invoiceRequest) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new InvoiceException(
                "INVOICE_NOT_FOUND_BY_ID",
                "Invoice with id " + id + "does not exist",
                HttpStatus.NOT_FOUND
            ));
        
        if (invoiceRequest.getCustomer() != null) invoice.setCustomerId(invoiceRequest.getCustomer());
        if (invoiceRequest.getDetail()!= null) invoice.setDetail(invoiceRequest.getDetail());
        if (invoiceRequest.getAmount() != null) invoice.setAmount(invoiceRequest.getAmount());
        if (invoiceRequest.getNumber() != null) invoice.setNumber(invoiceRequest.getNumber());
        
        return invoiceResponseMapper.InvoiceToInvoiceResponse(invoiceRepository.save(invoice));
    }
    
    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest) {
        Invoice invoiceToSave = invoiceRequestMapper.InvoiceRequestToInvoice(invoiceRequest);
        Invoice savedInvoice = invoiceRepository.save(invoiceToSave);
        return invoiceResponseMapper.InvoiceToInvoiceResponse(savedInvoice);
    }
    
    public InvoiceResponse deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new InvoiceException(
                "INVOICE_NOT_FOUND_BY_ID",
                "Invoice with id " + id + "does not exist",
                HttpStatus.NOT_FOUND
            ));
        
        invoiceRepository.deleteById(id);
        return invoiceResponseMapper.InvoiceToInvoiceResponse(invoice);
    }
}
