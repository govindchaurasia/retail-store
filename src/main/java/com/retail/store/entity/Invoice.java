package com.retail.store.entity;

import static com.retail.store.enums.ProductCategory.GROCERY;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.retail.store.enums.DiscountType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice")
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
	
    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "invoice_line_items", joinColumns = {@JoinColumn(name = "invoice_id_fk")},
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<InvoiceLineItems> lineItems;
    
	public Invoice(UserMaster user) {
		this.userMaster = user;
		this.setCreatedOn(new Date());
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
}
