package com.paymentchain.billing.mappers;

import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:34:32+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class InvoiceResponseMapperImpl implements InvoiceResponseMapper {

    @Override
    public Invoice InvoiceResponseToInvoice(InvoiceResponse source) {
        if ( source == null ) {
            return null;
        }

        Invoice invoice = new Invoice();

        if ( source.getId() != null ) {
            invoice.setId( source.getId() );
        }
        if ( source.getCustomerId() != null ) {
            invoice.setCustomerId( source.getCustomerId() );
        }
        invoice.setNumber( source.getNumber() );
        invoice.setDetail( source.getDetail() );
        if ( source.getAmount() != null ) {
            invoice.setAmount( source.getAmount() );
        }

        return invoice;
    }

    @Override
    public List<Invoice> InvoiceResponseListToInvoiceList(List<InvoiceResponse> source) {
        if ( source == null ) {
            return null;
        }

        List<Invoice> list = new ArrayList<Invoice>( source.size() );
        for ( InvoiceResponse invoiceResponse : source ) {
            list.add( InvoiceResponseToInvoice( invoiceResponse ) );
        }

        return list;
    }

    @Override
    public InvoiceResponse InvoiceToInvoiceResponse(Invoice source) {
        if ( source == null ) {
            return null;
        }

        InvoiceResponse invoiceResponse = new InvoiceResponse();

        invoiceResponse.setId( source.getId() );
        invoiceResponse.setCustomerId( source.getCustomerId() );
        invoiceResponse.setNumber( source.getNumber() );
        invoiceResponse.setDetail( source.getDetail() );
        invoiceResponse.setAmount( source.getAmount() );

        return invoiceResponse;
    }

    @Override
    public List<InvoiceResponse> InvoicetListToInvoiceResponseList(List<Invoice> source) {
        if ( source == null ) {
            return null;
        }

        List<InvoiceResponse> list = new ArrayList<InvoiceResponse>( source.size() );
        for ( Invoice invoice : source ) {
            list.add( InvoiceToInvoiceResponse( invoice ) );
        }

        return list;
    }
}
