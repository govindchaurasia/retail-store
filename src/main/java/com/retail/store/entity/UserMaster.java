package com.retail.store.entity;

import static javax.persistence.EnumType.STRING;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.retail.store.enums.UserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_master")
public class UserMaster extends BaseEntity {
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "user_type")
	@Enumerated(STRING)
	private UserType userType;

}
