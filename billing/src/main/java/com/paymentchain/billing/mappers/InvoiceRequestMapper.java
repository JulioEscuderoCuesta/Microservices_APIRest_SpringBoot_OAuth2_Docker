/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.paymentchain.billing.mappers;

import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.entities.Invoice;
import java.util.List;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 *
 * @author Julio Escudero Cuesta
 */
@Mapper(componentModel = "spring")
public interface InvoiceRequestMapper {
    
    @Mappings({@Mapping(source = "customer", target = "customerId")})
    Invoice InvoiceRequestToInvoice(InvoiceRequest source);
    
    List<Invoice> InvoiceRequestListToInvoiceList(InvoiceRequest source);
    
    @InheritInverseConfiguration
    InvoiceRequest InvoiceToInvoiceRequest(Invoice source);
    
    @InheritInverseConfiguration
    List<InvoiceRequest> InvoicetListToInvoiceRequestList(Invoice source);
}
