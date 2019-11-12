package com.coupons.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coupons.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

	// Query -> give me all the coupons by specific customerId.
	@Query("SELECT c FROM Customer cust JOIN cust.coupons c WHERE cust.id = :customerId")
	List<Coupon> findAllCouponsByCustomerId(long customerId);

	// The different between the function in company we have column in the table and
	// because of that we don't have to ask in a Query.
	List<Coupon> findAllCouponsByCompanyId(long companyId);

	
	List<Coupon> findAllByEndDateBefore(Date date);

}
