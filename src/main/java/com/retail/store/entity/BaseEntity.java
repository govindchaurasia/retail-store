package com.retail.store.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "createdOn", columnDefinition = "TIMESTAMP (3)", nullable = false)
	private Date createdOn;

	@Column(name = "updatedOn", columnDefinition = "TIMESTAMP (3)", nullable = false)
	private Date updatedOn;
}
