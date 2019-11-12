package com.coupons.service;

import java.util.List;

import com.coupons.entity.Company;
import com.coupons.entity.Coupon;
import com.coupons.entity.Customer;
import com.coupons.rest.controller.ex.EmailAlreadyExistException;

public interface AdminService {

	Customer saveCustomer(Customer customer) throws EmailAlreadyExistException;

	Company saveCompany(Company company) throws EmailAlreadyExistException;

	Customer findCustomerById(long customerId);

	Coupon findCouponById(long couponId);

	Company findCompanyById(long companyId);

	List<Customer> findAllCustomers();

	List<Company> findAllComapnies();

	List<Coupon> findAllCoupons();

	List<Coupon> findCompanyCoupons(long companyId);

	List<Coupon> findCustomerCoupons(long customerId);

	void deleteCompanyById(long companyId);

	void deleteCouponById(long couponId);

	void deleteCustomerById(long customerId);

}
