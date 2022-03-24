package com.retail.store.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.retail.store.enums.DiscountType;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity
@Table(name = "invoice")
public class Invoice extends BaseEntity {

	@OneToOne(fetch = EAGER)
	@JoinColumn(name = "user_id_fk")
	private User user;
	
	@Column(name = "total_before_discount")
	private Integer totalBeforeDiscount;
	
	@Column(name = "discount_type")
	@Enumerated(STRING)
	private DiscountType discountType;
	
	@Column(name = "invoice_date")
	private Date invoiceDate;
	
	public Invoice(User user) {
		this.user = user;
	}
}
