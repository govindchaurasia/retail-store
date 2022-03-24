package com.retail.store.service;

import static com.retail.store.enums.UserType.AFFILIATE;
import static com.retail.store.enums.UserType.EMPLOYEE;
import static com.retail.store.enums.ProductCategory.GROCERY;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.store.dto.InvoiceRequest;
import com.retail.store.dto.ProductDTO;
import com.retail.store.entity.Invoice;
import com.retail.store.entity.InvoiceLineItems;
import com.retail.store.entity.Product;
import com.retail.store.entity.User;
import com.retail.store.repository.InvoiceDetailRepository;
import com.retail.store.repository.InvoiceRepository;

@Service
public class InvoiceService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;
	
	public void generateInvoice(InvoiceRequest invoiceRequest) {
		User user = userService.findUserById(invoiceRequest.getUserId());
		List<ProductDTO> productDtos = invoiceRequest.getProductDto();
		Integer totalPrice = saveInvoice(productDtos, user);
		Integer discountPrice = 0;
		LocalDate today = LocalDate.now();
		LocalDate invoiceDate = findFirstInvoiceByUser(user);
		if(Objects.equals(user.getUserType(), EMPLOYEE)) {
			discountPrice = discountByPercentage(totalPrice, 30);
		} else if(Objects.equals(user.getUserType(), AFFILIATE)) {
			discountPrice = discountByPercentage(totalPrice, 10);
		} else if(invoiceDate.isBefore(today.minusYears(2))) {
			discountPrice = discountByPercentage(totalPrice, 5);
		} else {
			discountPrice = discountByPrice(totalPrice, 5, 100);
		}
		
	}
	
	public Integer saveInvoice(List<ProductDTO> productDtos, User user) {
		Invoice invoice = invoiceRepository.save(new Invoice(user));
		return saveInvoiceDetails(productDtos, invoice);
	}
	
	public Integer saveInvoiceDetails(List<ProductDTO> productDtos, Invoice invoice) {
		List<InvoiceLineItems> invoiceItems = new ArrayList<>();
		for (ProductDTO productDTO : productDtos) {
			InvoiceLineItems items = new InvoiceLineItems();
			Product product = productService.findById(productDTO.getProductId());
			items.setInvoice(invoice);
			items.setProduct(product);
			items.setProductQty(productDTO.getQty());
			items.setProductPrice(product.getPrice() * productDTO.getQty());
			
			invoiceItems.add(items);
		}
		invoiceDetailRepository.saveAll(invoiceItems);
		return invoiceItems.parallelStream().filter(l -> !l.getProduct().getProductCategory().equals(GROCERY)).collect(Collectors.summingInt(InvoiceLineItems::getProductPrice));
	}
	
	private LocalDate findFirstInvoiceByUser(User user) {
		Invoice invoice = invoiceRepository.findFirstByUserIdOrderByIdAsc(user.getId());
		return convertToLocalDate(invoice.getInvoiceDate());
	}
	
	private int discountByPercentage(int totalPrice, int discountInPercent) {
		return totalPrice - (totalPrice * discountInPercent / 100);
	}
	
	private int discountByPrice(int totalPrice, int discountInPrice, int price) {
		return (totalPrice/price) * discountInPrice;
	}
	
	public LocalDate convertToLocalDate(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
}
