package com.coupons.service;

import java.util.List;

import com.coupons.entity.Coupon;
import com.coupons.entity.Customer;

public interface CustomerService {

	List<Coupon> findAllCoupons();

	List<Coupon> findCustomerCoupons();

	Coupon purchaseCoupon(long couponId);

	Customer findCurrentCustomer();

	Coupon findCouponById(long id);

	

}
