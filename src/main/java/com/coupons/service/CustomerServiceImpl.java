package com.coupons.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.coupons.entity.Coupon;
import com.coupons.entity.Customer;
import com.coupons.repository.CouponRepository;
import com.coupons.repository.CustomerRepository;

@Service
@Scope("prototype")
public class CustomerServiceImpl extends AbsService implements CustomerService {

	private long customerId;

	private CouponRepository couponRepository;
	private CustomerRepository customerRepository;

	@Autowired
	public CustomerServiceImpl(CouponRepository couponRepository, CustomerRepository customerRepository) {
		this.couponRepository = couponRepository;
		this.customerRepository = customerRepository;
	}

	@Override
	public List<Coupon> findAllCoupons() {
		return couponRepository.findAll();
	}

	@Override
	public List<Coupon> findCustomerCoupons() {
		return couponRepository.findAllCouponsByCustomerId(customerId);
	}

	@Override
	public Coupon purchaseCoupon(long couponId) {
		Optional<Customer> customerOp = customerRepository.findById(customerId);
		Optional<Coupon> couponOp = couponRepository.findById(couponId);

		if (customerOp.isPresent() && couponOp.isPresent()) {
			Customer customer = customerOp.get();
			Coupon coupon = couponOp.get();

			customer.addCoupon(coupon);
			coupon.setAmount(coupon.getAmount() - 1);
			customerRepository.save(customer);
			return coupon;
		}
		return null;
	}

	@Override
	public Customer findCurrentCustomer() {
		Optional<Customer> customer = customerRepository.findById(customerId);

		if (customer.isPresent()) {
			return customer.get();
		}
		return null;
	}

	@Override
	public Coupon findCouponById(long id) {
		return couponRepository.findById(id).orElse(null);
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

}
