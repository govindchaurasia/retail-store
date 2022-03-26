package com.retail.store.service;

import com.retail.store.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.store.entity.Product;
import com.retail.store.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product findById(Long productId) {
		return productRepository.findById(productId).orElse(null);
	}

	public Map<Long, Product> findByIds(List<Long> productIds) {
		List<Product> productList = productRepository.findAllById(productIds);
		return productList.stream().collect(Collectors.toMap(BaseEntity::getId, p -> p, (p1, p2) -> p1));
	}
}
