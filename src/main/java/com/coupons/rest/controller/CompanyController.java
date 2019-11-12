package com.coupons.rest.controller;

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
import com.coupons.rest.ClientSession;
import com.coupons.rest.controller.ex.IllegalCouponException;
import com.coupons.rest.controller.ex.InvalidLoginException;
import com.coupons.rest.controller.ex.NoSuchCompanyException;
import com.coupons.rest.controller.ex.NoSuchCouponException;
import com.coupons.rest.controller.ex.TitleAlreadyExistException;
import com.coupons.rest.controller.ex.TitleOrDateMissingException;
import com.coupons.service.CompanyService;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CompanyController {

	private Map<String, ClientSession> map;

	@Autowired
	public CompanyController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.map = tokensMap;
	}

	private CompanyService getService(String token) throws InvalidLoginException {
		ClientSession session = map.get(token);

		if (session == null) {
			throw new InvalidLoginException("The login is timed out, please login again.");
		}

		session.accessed();

		return (CompanyService) session.getService();
	}

	@GetMapping("/companies/{token}")
	public ResponseEntity<Company> findCurrentCompany(@PathVariable String token) throws InvalidLoginException,
			NoSuchCompanyException {

		Company company = checkIfCompanyExists(token);

		return ResponseEntity.ok(company);
	}

	@GetMapping("/companies/coupons/{id}/{token}")
	public ResponseEntity<Coupon> findCouponById(@PathVariable String token, @PathVariable long id)
			throws InvalidLoginException, NoSuchCouponException, IllegalCouponException {

		Coupon coupon = checkIfCouponBelongToCompany(token, id);
		return ResponseEntity.ok(coupon);
	}

	@GetMapping("/companies/coupons/{token}")
	public ResponseEntity<List<Coupon>> findCompanyCoupons(@PathVariable String token) throws InvalidLoginException {
		List<Coupon> coupons = getService(token).findCompanyCoupons();

		if (!coupons.isEmpty()) {
			return ResponseEntity.ok(coupons);
		}
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/companies/coupons/{token}")
	public ResponseEntity<Coupon> addCoupon(@PathVariable String token, @RequestBody Coupon coupon)
			throws InvalidLoginException, TitleAlreadyExistException {

		checkIfCouponTitleExists(token, coupon);

		coupon.setId(0);
		getService(token).saveCoupon(coupon);
		return ResponseEntity.ok(coupon);
	}

	@PutMapping("/companies/coupons/{token}")
	public ResponseEntity<Coupon> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon)
			throws InvalidLoginException, TitleAlreadyExistException, NoSuchCouponException, IllegalCouponException, TitleOrDateMissingException {

		checkIfCouponBelongToCompany(token, coupon.getId());
		
		checkIfCouponTitleOrDateIsNull(token, coupon);

		checkUpdateTitle(token, coupon);
		
		getService(token).saveCoupon(coupon);
		
		return ResponseEntity.ok(coupon);
	}


	@DeleteMapping("/companies/coupons/{id}/{token}")
	public void deleteCouponById(@PathVariable String token, @PathVariable long id) throws InvalidLoginException,
			NoSuchCouponException, IllegalCouponException {

		checkIfCouponExists(token, id);

		checkIfCouponBelongToCompany(token, id);

		getService(token).deleteCouponById(id);
	};

	// ------------------------------------------------------------------------------------------------
	private Company checkIfCompanyExists(String token) throws InvalidLoginException, NoSuchCompanyException {
		Company company = getService(token).findCurrentCompany();

		if (company == null) {
			throw new NoSuchCompanyException("Unable to find company!");
		}
		return company;
	}

	private Coupon checkIfCouponBelongToCompany(String token, long id) throws InvalidLoginException,
			NoSuchCouponException, IllegalCouponException {
		
		Coupon coupon = checkIfCouponExists(token, id);
		Company comp = getService(token).findCurrentCompany();
		
		if (comp.getId() != coupon.getCompany().getId()) {
			throw new IllegalCouponException("Unable to continue, this coupon is not of this company.");
		}
		return coupon;
	}

	private Coupon checkIfCouponExists(String token, long id) throws InvalidLoginException, NoSuchCouponException {
		
		Coupon coupon = getService(token).findCouponById(id);
		
		if (coupon == null) {
			throw new NoSuchCouponException("Unable to find coupon!");
		}
		return coupon;
	}

	private void checkIfCouponTitleExists(String token, Coupon coupon) throws InvalidLoginException, TitleAlreadyExistException {
		
		List<Coupon> allCoupons = getService(token).findAllCoupons();

		for (Coupon coup : allCoupons) {
			if (coup.getTitle().equals(coupon.getTitle())) {
				throw new TitleAlreadyExistException(
						String.format("The title '%s' is already exist. please change and try again", coup.getTitle()));
			}
		}
	}

	private void checkIfCouponTitleOrDateIsNull(String token, Coupon coupon) throws TitleOrDateMissingException, InvalidLoginException {
		
		getService(token);
		
		if (coupon.getTitle() == null || coupon.getEndDate()== null) {
			throw new TitleOrDateMissingException("title or date are missing");
		}
	}

	private void checkUpdateTitle(String token, Coupon coupon) throws InvalidLoginException, IllegalCouponException {
		Coupon coupon2 = getService(token).findCouponById(coupon.getId());
		if (coupon2.getTitle() != coupon.getTitle()) {
			throw new IllegalCouponException("Unable to update ,changing title is not alowed!");
		}
	}
}
