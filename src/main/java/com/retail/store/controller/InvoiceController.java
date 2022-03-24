package com.retail.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.store.dto.InvoiceRequest;
import com.retail.store.entity.Invoice;
import com.retail.store.service.InvoiceService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/")
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	@RequestMapping("/generate-invoice")
	public ResponseEntity<Invoice> generateInvoice(@RequestBody InvoiceRequest invoiceRequest) {
		return ResponseEntity.ok(invoiceService.generateInvoice(invoiceRequest));
	}
}
