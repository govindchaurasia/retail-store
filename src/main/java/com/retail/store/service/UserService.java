package com.retail.store.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.store.entity.UserMaster;
import com.retail.store.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public UserMaster findUserById(Long id) {
		Optional<UserMaster> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			UserMaster user = optionalUser.get();
			return user;
		} else {
			throw new RuntimeException("User not found");
		}
	}
}
