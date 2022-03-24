package com.retail.store.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "created_on", columnDefinition = "TIMESTAMP (3)")
	private Date createdOn;

	@Column(name = "updated_on", columnDefinition = "TIMESTAMP (3)")
	private Date updatedOn;
}
