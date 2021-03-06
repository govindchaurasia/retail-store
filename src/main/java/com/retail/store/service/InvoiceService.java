package com.retail.store.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.store.dto.InvoiceRequest;
import com.retail.store.dto.ProductDTO;
import com.retail.store.entity.Invoice;
import com.retail.store.entity.Product;
import com.retail.store.entity.UserMaster;
import com.retail.store.repository.InvoiceRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class InvoiceService {
	private UserService userService;
	private ProductService productService;
	private InvoiceRepository invoiceRepository;

	@Autowired
	public InvoiceService(UserService userService, ProductService productService, InvoiceRepository invoiceRepository) {
		this.userService = userService;
		this.productService = productService;
		this.invoiceRepository = invoiceRepository;
	}

	@Transactional
	public Invoice generateInvoice(InvoiceRequest invoiceRequest) {
		UserMaster user = userService.findUserById(invoiceRequest.getUserId());
		List<ProductDTO> productDtos = invoiceRequest.getProductDto();
		Invoice invoice = saveInvoice(productDtos, user);
		return invoice;
	}

	private Invoice saveInvoice(List<ProductDTO> productDtos, UserMaster user) {
		List<Long> productIds = productDtos.stream().map(ProductDTO::getProductId).collect(toList());
		Map<Long, Product> productMap = productService.findByIds(productIds);
		LocalDate firstInvoiceDateByUser = findFirstInvoiceByUser(user);
		Invoice savedInvoice = invoiceRepository.save(new Invoice(user, productDtos, productMap, firstInvoiceDateByUser));
		log.debug("savedInvoice Id: " + savedInvoice);
		return savedInvoice;
	}

	private LocalDate findFirstInvoiceByUser(UserMaster user) {
		Invoice invoice = invoiceRepository.findFirstByUserMasterIdOrderByIdAsc(user.getId());
		return invoice != null? convertToLocalDate(invoice.getInvoiceDate()): null;
	}

	private LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}
}
