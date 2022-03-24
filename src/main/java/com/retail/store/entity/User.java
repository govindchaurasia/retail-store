package com.retail.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.retail.store.enums.UserType;

import lombok.Getter;
import lombok.Setter;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "user_type")
	@Enumerated(STRING)
	private UserType userType;

}
