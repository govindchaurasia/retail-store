package com.retail.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.retail.store.enums.ProductCategory;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "category")
	@Enumerated(STRING)
	private ProductCategory productCategory;
	
	@Column(name = "unit")
	private Integer unit;
	
	@Column(name = "price")
	private Integer price;
}
