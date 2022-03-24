package com.retail.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.store.dto.InvoiceRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/invoice")
public class InvoiceController {

	@RequestMapping("/generate")
	public ResponseEntity generateInvoice(@RequestBody InvoiceRequest invoiceRequest) {
		
	}
}
