package com.retail.store.entity;

import static com.retail.store.enums.DiscountType.PERCENTAGE;
import static com.retail.store.enums.DiscountType.PRICE;
import static com.retail.store.enums.DiscountType.NONE;
import static com.retail.store.enums.ProductCategory.GROCERY;
import static com.retail.store.enums.UserType.AFFILIATE;
import static com.retail.store.enums.UserType.EMPLOYEE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.retail.store.dto.ProductDTO;
import com.retail.store.enums.DiscountType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice")
@NoArgsConstructor
public class Invoice extends BaseEntity {

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "user_id_fk")
    private UserMaster userMaster;

    @Column(name = "total_before_discount")
    private Integer totalBeforeDiscount;

    @Column(name = "final_amount")
    private Integer finalAmount;

    @Column(name = "discount_type")
    @Enumerated(STRING)
    private DiscountType discountType;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "invoice_date")
    private Date invoiceDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoice", orphanRemoval = true)
    private List<InvoiceLineItems> lineItems;

    public Invoice(UserMaster user) {
        this.userMaster = user;
        this.setCreatedOn(new Date());
    }

    public Invoice(UserMaster user, List<ProductDTO> productDtos, Map<Long, Product> productMap,
                   LocalDate firstInvoiceDateByUser) {
        this(user);
        this.lineItems = productDtos.stream().map(productDTO -> {
            InvoiceLineItems invoiceLineItems = new InvoiceLineItems();
            Product originalProduct = productMap.get(productDTO.getProductId());
            invoiceLineItems.setInvoice(this);
            invoiceLineItems.setProduct(originalProduct);
            invoiceLineItems.setProductQty(productDTO.getQty());
            invoiceLineItems.setProductPrice(originalProduct.getPrice() * productDTO.getQty());
            invoiceLineItems.setCreatedOn(new Date());
            return invoiceLineItems;
        }).collect(Collectors.toList());

        Integer discountPrice = 0;
        LocalDate today = LocalDate.now();

        Integer totalPriceWithoutGrocery = this.getTotalPriceWithoutGrocery();
        
        if(totalPriceWithoutGrocery != 0) {
        	
			if (Objects.equals(user.getUserType(), EMPLOYEE)) {
				discountPrice = discountByPercentage(totalPriceWithoutGrocery, 30);
				setDiscountDetails(30, PERCENTAGE, discountPrice);
			} else if (Objects.equals(user.getUserType(), AFFILIATE)) {
				discountPrice = discountByPercentage(totalPriceWithoutGrocery, 10);
				setDiscountDetails(10, PERCENTAGE, discountPrice);
			} else if (firstInvoiceDateByUser != null && firstInvoiceDateByUser.isBefore(today.minusYears(2))) {
				discountPrice = discountByPercentage(totalPriceWithoutGrocery, 5);
				setDiscountDetails(5, PERCENTAGE, discountPrice);
			} else {
				discountPrice = discountByPrice(totalPriceWithoutGrocery, 5, 100);
				setDiscountDetails(discountPrice, PRICE, totalPriceWithoutGrocery - discountPrice);
			}
        } else {
        	this.setDiscount(0);
        	this.setDiscountType(NONE);
        	this.setFinalAmount(this.getTotalPrice());
        }
        this.setInvoiceDate(new Date());
        this.setTotalBeforeDiscount(this.getTotalPrice());
    }

    private Integer discountByPrice(Integer totalPrice, Integer discountInPrice, Integer price) {
        return (totalPrice / price) * discountInPrice;
    }

    private Integer discountByPercentage(Integer totalPrice, Integer discountInPercent) {
        return totalPrice - (totalPrice * discountInPercent / 100);
    }

    @Transient
    public Integer getTotalPrice() {
        return lineItems.parallelStream().collect(Collectors.summingInt(InvoiceLineItems::getProductPrice));
    }

    @Transient
    public Integer getTotalGroceryPrice() {
        return lineItems.parallelStream().filter(l -> l.getProduct().getProductCategory().equals(GROCERY)).collect(Collectors.summingInt(InvoiceLineItems::getProductPrice));
    }

    @Transient
    public Integer getTotalPriceWithoutGrocery() {
        return lineItems.parallelStream().filter(l -> !l.getProduct().getProductCategory().equals(GROCERY)).collect(Collectors.summingInt(InvoiceLineItems::getProductPrice));
    }
    
    private void setDiscountDetails(Integer discount, DiscountType discountType, Integer discountedPrice) {
    	this.setDiscount(discount);
    	this.setDiscountType(discountType);
    	this.setFinalAmount(discountedPrice + getTotalGroceryPrice());
    }
}
