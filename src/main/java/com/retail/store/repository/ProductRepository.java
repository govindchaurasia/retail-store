package com.retail.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.store.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
