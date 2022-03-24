package com.retail.store.service;

import static com.retail.store.enums.DiscountType.PERCENTAGE;
import static com.retail.store.enums.DiscountType.PRICE;
import static com.retail.store.enums.UserType.AFFILIATE;
import static com.retail.store.enums.UserType.EMPLOYEE;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.store.dto.InvoiceRequest;
import com.retail.store.dto.ProductDTO;
import com.retail.store.entity.Invoice;
import com.retail.store.entity.InvoiceLineItems;
import com.retail.store.entity.Product;
import com.retail.store.entity.UserMaster;
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
	
	public Invoice generateInvoice(InvoiceRequest invoiceRequest) {
		UserMaster user = userService.findUserById(invoiceRequest.getUserId());
		List<ProductDTO> productDtos = invoiceRequest.getProductDto();
		saveInvoice(productDtos, user);
		Invoice invoice = findByUser(user);
		Integer discountPrice = 0;
		LocalDate today = LocalDate.now();

		Integer totalPriceWithoutGrocery = invoice.getTotalPriceWithoutGrocery();
		LocalDate invoiceDate = findFirstInvoiceByUser(user);
		if(Objects.equals(user.getUserType(), EMPLOYEE)) {
			discountPrice = discountByPercentage(totalPriceWithoutGrocery, 30);
			invoice.setDiscountType(PERCENTAGE);
			invoice.setDiscount(30);
			invoice.setFinalAmount(discountPrice);
		} else if(Objects.equals(user.getUserType(), AFFILIATE)) {
			discountPrice = discountByPercentage(totalPriceWithoutGrocery, 10);
			invoice.setDiscountType(PERCENTAGE);
			invoice.setDiscount(10);
			invoice.setFinalAmount(discountPrice);
		} else if(invoiceDate.isBefore(today.minusYears(2))) {
			discountPrice = discountByPercentage(totalPriceWithoutGrocery, 5);
			invoice.setDiscountType(PERCENTAGE);
			invoice.setDiscount(5);
			invoice.setFinalAmount(discountPrice);
		} else {
			discountPrice = discountByPrice(totalPriceWithoutGrocery, 5, 100);
			invoice.setDiscountType(PRICE);
			invoice.setDiscount(discountPrice);
			invoice.setFinalAmount(totalPriceWithoutGrocery-discountPrice);
		}
		invoice.setInvoiceDate(new Date());
		invoice.setTotalBeforeDiscount(invoice.getTotalGroceryPrice() + invoice.getFinalAmount());
		
		return invoice;
	}
	
	public void saveInvoice(List<ProductDTO> productDtos, UserMaster user) {
		Invoice invoice = invoiceRepository.save(new Invoice(user));
		saveInvoiceDetails(productDtos, invoice);	
	}
	
	public void saveInvoiceDetails(List<ProductDTO> productDtos, Invoice invoice) {
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
		
	}
	
	private LocalDate findFirstInvoiceByUser(UserMaster user) {
		Invoice invoice = invoiceRepository.findFirstByUserMasterIdOrderByIdAsc(user.getId());
		return convertToLocalDate(invoice.getInvoiceDate());
	}
	
	private Invoice findByUser(UserMaster user) {
		return invoiceRepository.findByUserMasterId(user.getId());
	}
	
	private Integer discountByPercentage(Integer totalPrice, Integer discountInPercent) {
		return totalPrice - (totalPrice * discountInPercent / 100);
	}
	
	private Integer discountByPrice(Integer totalPrice, Integer discountInPrice, Integer price) {
		return (totalPrice/price) * discountInPrice;
	}
	
	private LocalDate convertToLocalDate(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}

}
