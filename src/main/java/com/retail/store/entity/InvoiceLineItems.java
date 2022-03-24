package com.retail.store.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.FetchType.EAGER;

import javax.persistence.Column;

@Getter
@Setter
@Entity
@Table(name = "invoice_line_items")
public class InvoiceLineItems extends BaseEntity {

	@OneToOne(fetch = EAGER)
	@JoinColumn(name = "invoice_id_fk")
	private Invoice invoice;
	
	@OneToOne(fetch = EAGER)
	@JoinColumn(name = "product_id_fk")
	private Product product;
	
	@Column(name = "product_qty")
	private Integer productQty;
	
	@Column(name = "product_price")
	private Integer productPrice;
}
