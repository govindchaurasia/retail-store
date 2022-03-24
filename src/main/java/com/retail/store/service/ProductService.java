package com.retail.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.store.entity.Product;
import com.retail.store.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public Product findById(Long productId) {
		return productRepository.findById(productId).orElse(null);
	}
}
