package com.coupons.rest.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coupons.entity.Company;
import com.coupons.entity.Coupon;
import com.coupons.entity.Customer;
import com.coupons.rest.ClientSession;
import com.coupons.rest.controller.ex.EmailAlreadyExistException;
import com.coupons.rest.controller.ex.FaildCreateObjectException;
import com.coupons.rest.controller.ex.InvalidLoginException;
import com.coupons.rest.controller.ex.NoSuchCompanyException;
import com.coupons.rest.controller.ex.NoSuchCouponException;
import com.coupons.rest.controller.ex.NoSuchCustomerException;
import com.coupons.rest.controller.ex.UnableToChangeEmailException;
import com.coupons.service.AdminService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class AdminController {

	/**
	 * This map filed storage -> token, session.
	 */
	private Map<String, ClientSession> map;

	/**
	 * This constructor initialize the
	 */
	@Autowired
	public AdminController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.map = tokensMap;
	}

	private AdminService getService(String token) throws InvalidLoginException {
		ClientSession clientSession = map.get(token);

		if (clientSession == null) {
			throw new InvalidLoginException("The login is timed out, please login again.");
		}

		clientSession.accessed();

		return (AdminService) clientSession.getService();
	}

	@PostMapping("/admin/customers/{token}")
	public ResponseEntity<Customer> addCustomer(@PathVariable String token, @RequestBody Customer customer)
			throws InvalidLoginException, EmailAlreadyExistException, FaildCreateObjectException {

		checkValidCustomer(token, customer);

		checkValidCustomerEmail(token, customer);

		customer.setId(0);
		getService(token).saveCustomer(customer);
		return ResponseEntity.ok(customer);
	}

	@PutMapping("/admin/customers/{token}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable String token, @RequestBody Customer customer)
			throws InvalidLoginException, EmailAlreadyExistException, UnableToChangeEmailException,
			NoSuchCustomerException, FaildCreateObjectException {

		checkIfCustomerValidToUpdate(token, customer);

		getService(token).saveCustomer(customer);
		return ResponseEntity.ok(customer);

	}

	@PostMapping("/admin/companies/{token}")
	public ResponseEntity<Company> addCompany(@PathVariable String token, @RequestBody Company company)
			throws InvalidLoginException, EmailAlreadyExistException, FaildCreateObjectException {

		checkValidCompany(token, company);

		checkValidCompanyEmail(token, company);

		company.setId(0);
		getService(token).saveCompany(company);
		return ResponseEntity.ok(company);
	}

	@PutMapping("/admin/companies/{token}")
	public ResponseEntity<Company> updateCompany(@PathVariable String token, @RequestBody Company company)
			throws InvalidLoginException, EmailAlreadyExistException, UnableToChangeEmailException,
			NoSuchCompanyException {

		checkIfCompanyExists(token, company.getId());

		checkIfCompanyEmailChange(token, company);

		getService(token).saveCompany(company);
		return ResponseEntity.ok(company);
	}

	@GetMapping("/admin/customers/{id}/{token}")
	public ResponseEntity<Customer> findCustomerById(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException, NoSuchCustomerException {

		checkIfCustomerExists(token, id);

		Customer customer = getService(token).findCustomerById(id);

		return ResponseEntity.ok(customer);

	}

	@GetMapping("/admin/coupons/{id}/{token}")
	public ResponseEntity<Coupon> findCouponById(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException, NoSuchCouponException {

		Coupon coupon = checkIfCouponExists(token, id);

		return ResponseEntity.ok(coupon);
	}

	@GetMapping("/admin/companies/{id}/{token}")
	public ResponseEntity<Company> findCompanyById(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException, NoSuchCompanyException {

		checkIfCompanyExists(token, id);

		Company company = getService(token).findCompanyById(id);

		return ResponseEntity.ok(company);

	}

	@GetMapping("/admin/customers/{token}")
	public ResponseEntity<Collection<Customer>> findAllCustomers(@PathVariable String token)
			throws InvalidLoginException {
		List<Customer> allCustomers = getService(token).findAllCustomers();

		if (allCustomers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCustomers);
	}

	@GetMapping("/admin/companies/{token}")
	public ResponseEntity<Collection<Company>> findAllComapnies(@PathVariable String token)
			throws InvalidLoginException {
		List<Company> allComapnies = getService(token).findAllComapnies();

		if (allComapnies.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allComapnies);
	}

	@GetMapping("/admin/coupons/{token}")
	public ResponseEntity<Collection<Coupon>> findAllCoupons(@PathVariable String token) throws InvalidLoginException {
		List<Coupon> allCoupons = getService(token).findAllCoupons();

		if (allCoupons.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCoupons);
	}

	@GetMapping("/admin/companies/{id}/coupons/{token}")
	public ResponseEntity<Collection<Coupon>> findCompanyCoupons(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException, NoSuchCompanyException {

		checkIfCompanyExists(token, id);

		List<Coupon> companyCoupons = getService(token).findCompanyCoupons(id);

		if (companyCoupons.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(companyCoupons);
	}

	@GetMapping("/admin/customers/{id}/coupons/{token}")
	public ResponseEntity<Collection<Coupon>> findCustomerCoupons(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException, NoSuchCustomerException {

		checkIfCustomerExists(token, id);

		List<Coupon> customerCoupons = getService(token).findCustomerCoupons(id);

		if (customerCoupons.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(customerCoupons);
	}

	@DeleteMapping("/admin/companies/{id}/{token}")
	public void deleteCompanyById(@PathVariable String token, @PathVariable long id) throws InvalidLoginException,
			NoSuchCompanyException {

		checkIfCompanyExists(token, id);

		getService(token).deleteCompanyById(id);
	};

	@DeleteMapping("/admin/coupons/{id}/{token}")
	public void deleteCouponById(@PathVariable String token, @PathVariable long id) throws InvalidLoginException,
			NoSuchCouponException {

		checkIfCouponExists(token, id);

		getService(token).deleteCouponById(id);
	};

	@DeleteMapping("/admin/customers/{id}/{token}")
	public void deleteCustomerById(@PathVariable String token, @PathVariable long id) throws InvalidLoginException,
			NoSuchCustomerException {

		checkIfCustomerExists(token, id);
		getService(token).deleteCustomerById(id);
	};

	// ----------------------------------------------------------------------------------------------
	private void checkValidCustomer(String token, Customer customer) throws FaildCreateObjectException,
			InvalidLoginException {

		getService(token);
		
		if (customer.getEmail() == null || customer.getPassword() == null) {
			throw new FaildCreateObjectException(" password or email are empty!");
		}

	}

	private void checkValidCompany(String token, Company company) throws FaildCreateObjectException {

		if (company.getEmail() == null || company.getPassword() == null) {
			throw new FaildCreateObjectException(" password or email are empty!");

		}
	}

	private void checkValidCustomerEmail(String token, Customer customer) throws EmailAlreadyExistException,
			InvalidLoginException {
		List<Customer> allCustomers = getService(token).findAllCustomers();

		for (Customer customerTemp : allCustomers) {
			if (customerTemp.getEmail().equals(customer.getEmail())) {
				throw new EmailAlreadyExistException(
						String.format("The '%s' is already exist!", customer.getEmail()));
			}
		}

	}

	private void checkValidCompanyEmail(String token, Company company) throws EmailAlreadyExistException,
			InvalidLoginException {
		List<Company> allComapnies = getService(token).findAllComapnies();
		for (Company companyTemp : allComapnies) {
			if (companyTemp.getEmail().equals(company.getEmail())) {
				throw new EmailAlreadyExistException(
						String.format("The '%s' is already exist!", company.getEmail()));
			}
		}

	}

	private void checkIfCompanyExists(String token, long id) throws InvalidLoginException, NoSuchCompanyException {
		Company comp = getService(token).findCompanyById(id);
		if (comp == null) {
			throw new NoSuchCompanyException("Unable to find, company not exists!");
		}
	}

	private void checkIfCustomerExists(String token, long id) throws InvalidLoginException, NoSuchCustomerException {
		Customer cust = getService(token).findCustomerById(id);
		if (cust == null) {
			throw new NoSuchCustomerException("Unable to find, customer not exists!");
		}
	}

	private void checkIfCustomerValidToUpdate(String token, Customer customer) throws InvalidLoginException,
			NoSuchCustomerException, UnableToChangeEmailException, FaildCreateObjectException {
		
		checkValidCustomer(token, customer);
		
		Customer customerOriginal = getService(token).findCustomerById(customer.getId());
		
		if (customerOriginal == null) {

			throw new NoSuchCustomerException("Ubable to get customer!");
		}

		else if (!customerOriginal.getEmail().equals(customer.getEmail())) {
			throw new UnableToChangeEmailException("Unable to change the email addres!");
		}
	}

	private void checkIfCompanyEmailChange(String token, Company company) throws InvalidLoginException,
			UnableToChangeEmailException {
		Company companyOriginal = getService(token).findCompanyById(company.getId());

		if (!companyOriginal.getEmail().equals(company.getEmail())) {
			throw new UnableToChangeEmailException("Unable to change the email addres!");
		}
	}

	private Coupon checkIfCouponExists(String token, long id) throws InvalidLoginException, NoSuchCouponException {
		Coupon coupon = getService(token).findCouponById(id);

		if (coupon == null) {
			throw new NoSuchCouponException("Coupon doesn't excest!");
		}
		return coupon;
	}
}
