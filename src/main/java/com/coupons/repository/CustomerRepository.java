package com.coupons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coupons.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

		Customer findCustomerByEmailAndPassword(String email, String password);

}
