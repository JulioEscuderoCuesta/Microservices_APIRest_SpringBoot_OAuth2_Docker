/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.billing.mappers;

import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import java.util.List;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 *
 * @author Hp
 */
@Mapper(componentModel = "spring")
public interface InvoiceResponseMapper {
    
    Invoice InvoiceResponseToInvoice(InvoiceResponse source);
    
    List<Invoice> InvoiceResponseListToInvoiceList(List<InvoiceResponse> source);
    
    @InheritInverseConfiguration
    InvoiceResponse InvoiceToInvoiceResponse(Invoice source);
    
    @InheritInverseConfiguration
    List<InvoiceResponse> InvoicetListToInvoiceResponseList(List<Invoice> source);
}
