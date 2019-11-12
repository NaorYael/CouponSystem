package com.coupons.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.coupons.entity.Company;
import com.coupons.entity.Coupon;
import com.coupons.entity.Customer;
import com.coupons.repository.CompanyRepository;
import com.coupons.repository.CouponRepository;
import com.coupons.repository.CustomerRepository;
import com.coupons.rest.controller.ex.EmailAlreadyExistException;

@Service
@Scope("prototype")
public class AdminServiceImpl extends AbsService implements AdminService {

	private CouponRepository couponRepository;
	private CompanyRepository companyRepository;
	private CustomerRepository customerRepository;

	@Autowired
	public AdminServiceImpl(CouponRepository couponRepository, CompanyRepository companyRepository,
			CustomerRepository customerRepository) {
		this.couponRepository = couponRepository;
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
	}

	@Override
	public Customer saveCustomer(Customer customer) throws EmailAlreadyExistException {
			return customerRepository.save(customer);
	}

	@Override
	public List<Customer> findAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Customer findCustomerById(long customerId) {
		return customerRepository.findById(customerId).orElse(null);
	}

	@Override
	public void deleteCustomerById(long customerId) {
		customerRepository.deleteById(customerId);
	}

	@Override
	public Coupon findCouponById(long couponId) {
		return couponRepository.findById(couponId).orElse(null);
	}

	@Override
	public Company saveCompany(Company company) throws EmailAlreadyExistException {

		return companyRepository.save(company);
	}

	@Override
	public Company findCompanyById(long companyId) {
		return companyRepository.findById(companyId).orElse(null);
	}

	@Override
	public void deleteCompanyById(long companyId) {
		companyRepository.deleteById(companyId);

	}

	@Override
	public void deleteCouponById(long couponId) {
		couponRepository.deleteById(couponId);

	}

	@Override
	public List<Coupon> findCompanyCoupons(long companyId) {
		return couponRepository.findAllCouponsByCompanyId(companyId);
	}

	@Override
	public List<Company> findAllComapnies() {
		return companyRepository.findAll();
	}

	@Override
	public List<Coupon> findAllCoupons() {
		return couponRepository.findAll();
	}

	@Override
	public List<Coupon> findCustomerCoupons(long customerId) {
		return couponRepository.findAllCouponsByCustomerId(customerId);
	}

}
