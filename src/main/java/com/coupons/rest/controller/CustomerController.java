package com.coupons.rest.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coupons.entity.Coupon;
import com.coupons.entity.Customer;
import com.coupons.rest.ClientSession;
import com.coupons.rest.controller.ex.AlreadyPurchaseCouponException;
import com.coupons.rest.controller.ex.InvalidLoginException;
import com.coupons.rest.controller.ex.NoSuchCouponException;
import com.coupons.rest.controller.ex.NoSuchCustomerException;
import com.coupons.service.CustomerService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CustomerController {

	private Map<String, ClientSession> map;

	@Autowired
	public CustomerController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.map = tokensMap;
	}

	private CustomerService getService(String token) throws InvalidLoginException {
		ClientSession session = map.get(token);

		if (session == null) {
			throw new InvalidLoginException("The login is timed out, please login again.");
		}

		session.accessed();

		return (CustomerService) session.getService();
	}

	@GetMapping("/customers/{token}")
	public ResponseEntity<Customer> findCurrentCustomer(@PathVariable String token) throws InvalidLoginException,
			NoSuchCustomerException {

		Customer customer = checkIfCustomerExists(token);

		return ResponseEntity.ok(customer);
	}

	@GetMapping("/customers/customerCoupons/{token}")
	public ResponseEntity<Collection<Coupon>> findCustomerCoupons(@PathVariable String token)
			throws InvalidLoginException {
		List<Coupon> customerCoupons = getService(token).findCustomerCoupons();

		if (customerCoupons.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(customerCoupons);

	}

	@PostMapping("/customers/purchaseCoupon/{couponId}/{token}")
	public Coupon purchaseCoupon(@PathVariable String token, @PathVariable long couponId) throws InvalidLoginException,
			AlreadyPurchaseCouponException {

		allCustomerCouponsById(token, couponId);

		return getService(token).purchaseCoupon(couponId);
	}

	@GetMapping("/customers/coupons/{id}/{token}")
	public ResponseEntity<Coupon> findCouponById(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException, NoSuchCouponException {
		Coupon coupon = checkIfCouponExists(token, id);

		return ResponseEntity.ok(coupon);
	}

	@GetMapping("/customers/coupons/{token}")
	public ResponseEntity<Collection<Coupon>> findAllCoupons(@PathVariable String token) throws InvalidLoginException {

		List<Coupon> allCoupons = getService(token).findAllCoupons();

		if (allCoupons.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(allCoupons);
	}

	// ---------------------------------------------------------------------------------------------------

	private Customer checkIfCustomerExists(String token) throws InvalidLoginException, NoSuchCustomerException {
		Customer customer = getService(token).findCurrentCustomer();

		if (customer == null) {
			throw new NoSuchCustomerException("Unable to find customer!");
		}
		return customer;
	}

	private Coupon checkIfCouponExists(String token, long id) throws InvalidLoginException, NoSuchCouponException {
		Coupon coupon = getService(token).findCouponById(id);

		if (coupon == null) {
			throw new NoSuchCouponException("Unable to find coupon!");
		}
		return coupon;
	}

	private void allCustomerCouponsById(String token, long couponId) throws InvalidLoginException,
			AlreadyPurchaseCouponException {
		List<Coupon> customerCoupons = getService(token).findCustomerCoupons();

		for (Coupon coupon : customerCoupons) {
			if (coupon.getId() == couponId) {
				throw new AlreadyPurchaseCouponException("Dear customer, this coupon already purchase!");
			}
		}
	}
}
