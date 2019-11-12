package com.coupons.service;

import java.util.List;

import com.coupons.entity.Company;
import com.coupons.entity.Coupon;

public interface CompanyService {

	Coupon saveCoupon(Coupon coupon);

	Company saveCompany(Company company);

	List<Coupon> findCompanyCoupons();

	Coupon findCouponById(long couponId);
	
	Company findCurrentCompany();

	List<Coupon> getCompmayCoupons();

	void deleteCouponById(long id);

	void setCompanyId(long companyId);

	List<Coupon> findAllCoupons();
	
	
}
