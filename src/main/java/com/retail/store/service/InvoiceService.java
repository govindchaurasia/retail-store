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

@Service
@Slf4j
public class InvoiceService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	public Invoice generateInvoice(InvoiceRequest invoiceRequest) {
		UserMaster user = userService.findUserById(invoiceRequest.getUserId());
		List<ProductDTO> productDtos = invoiceRequest.getProductDto();
		Invoice invoice = saveInvoice(productDtos, user);
		return invoice;
	}
	
	public Invoice saveInvoice(List<ProductDTO> productDtos, UserMaster user) {
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
	
    private static Integer discountByPrice(Integer totalPrice, Integer discountInPrice, Integer price) {
        return (totalPrice / price) * discountInPrice;
    }

    private static Integer discountByPercentage(Integer totalPrice, Integer discountInPercent) {
        return totalPrice - (totalPrice * discountInPercent / 100);
    }

    
    public static void main(String[] args) {
		System.out.println(discountByPercentage(2512, 30));
	}

}
