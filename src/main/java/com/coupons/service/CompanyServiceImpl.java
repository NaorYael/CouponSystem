package com.coupons.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.coupons.entity.Company;
import com.coupons.entity.Coupon;
import com.coupons.repository.CompanyRepository;
import com.coupons.repository.CouponRepository;

@Service
@Scope("prototype")
public class CompanyServiceImpl extends AbsService implements CompanyService {

	private long companyId;

	private CouponRepository couponRepository;
	private CompanyRepository companyRepository;

	@Autowired
	public CompanyServiceImpl(CouponRepository couponRepository, CompanyRepository companyRepository) {
		this.couponRepository = couponRepository;
		this.companyRepository = companyRepository;
	}

	@Override
	public Company saveCompany(Company company) {
		return companyRepository.save(company);
	}

	@Override
	public Coupon saveCoupon(Coupon coupon) {
		Optional<Company> company = companyRepository.findById(companyId);

		if (company.isPresent()) {
			coupon.setCompany(company.get());
			return couponRepository.save(coupon);
		}
		return null;
	}

	@Override
	public List<Coupon> findCompanyCoupons() {
		return couponRepository.findAllCouponsByCompanyId(companyId);
	}

	@Override
	public Coupon findCouponById(long couponId) {
		return couponRepository.findById(couponId).orElse(null);
	}

	@Override
	public Company findCurrentCompany() {
		return companyRepository.findById(companyId).orElse(null);
	}

	public List<Coupon> getCompmayCoupons() {

		List<Coupon> coupons = couponRepository.findAllCouponsByCompanyId(companyId);

		if (!coupons.isEmpty()) {
			return coupons;
		}
		return null;
	}

	@Override
	public void deleteCouponById(long id) {
		couponRepository.deleteById(id);
	}

	@Override
	public List<Coupon> findAllCoupons() {
		return couponRepository.findAll();
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
}
