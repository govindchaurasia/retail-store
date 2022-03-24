package com.retail.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.store.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	Invoice findFirstByUserMasterIdOrderByIdAsc(Long userId);
	
	Invoice findByUserMasterId(Long userId);
}
