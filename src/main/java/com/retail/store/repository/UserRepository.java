package com.retail.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.store.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
