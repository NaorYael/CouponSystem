package com.coupons.rest;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.coupons.entity.Company;
import com.coupons.entity.Customer;
import com.coupons.repository.CompanyRepository;
import com.coupons.repository.CustomerRepository;
import com.coupons.rest.controller.ex.InvalidLoginException;
import com.coupons.service.AdminServiceImpl;
import com.coupons.service.CompanyServiceImpl;
import com.coupons.service.CustomerServiceImpl;

@Service
public class CouponSystem {

	private ApplicationContext context;
	private CompanyRepository companyRepository;
	private CustomerRepository customerRepository;

	private SessionCleaner sessionCleaner;
	private CouponCleaner couponCleaner;

	@Autowired
	public void setSessionCleaner(SessionCleaner sessionCleaner) {
		this.sessionCleaner = sessionCleaner;
	}

	@Autowired
	public void setCouponCleaner(CouponCleaner couponCleaner) {
		this.couponCleaner = couponCleaner;
	}

	@Autowired
	public CouponSystem(ApplicationContext context,
			CompanyRepository companyRepository, CustomerRepository customerRepository) {
		this.context = context;
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
	}

	public ClientSession login(String email, String password, LoginType type) throws InvalidLoginException {
		switch (type) {
			case ADMIN:
				return adminLogin(email, password);

			case COMPANY:
				return companyLogin(email, password);

			case CUSTOMER:
				return customerLogin(email, password);

			default:throw new InvalidLoginException("Invalid Login!");
		}
		
	}

	private ClientSession customerLogin(String email, String password) throws InvalidLoginException {
		Customer customer = customerRepository.findCustomerByEmailAndPassword(email, password);

		requiredValidCustomer(customer);

		CustomerServiceImpl service = context.getBean(CustomerServiceImpl.class);
		// == CustomerServiceImpl service = new CustomerServiceImpl();
		service.setCustomerId(customer.getId());
		ClientSession session = context.getBean(ClientSession.class);
		// == ClientSession session = new ClientSession();
		session.setService(service);
		session.accessed();

		return session;
	}

	private void requiredValidCustomer(Customer customer) throws InvalidLoginException {
		if (customer == null) {
			throw new InvalidLoginException("Login faild, name or password are invalid.");
		}
	}

	private ClientSession companyLogin(String email, String password) throws InvalidLoginException {
		Company company = companyRepository.findCompanyByEmailAndPassword(email, password);

		requiredValidCompany(company);

		CompanyServiceImpl companyService = context.getBean(CompanyServiceImpl.class);
		//== CompanyServiceImpl companyService = new CompanyServiceImpl();
		companyService.setCompanyId(company.getId());
		ClientSession clientSession = context.getBean(ClientSession.class);
		//== ClientSession clientSession = new ClientSession();
		clientSession.setService(companyService);
		clientSession.accessed();

		return clientSession;
	}

	private void requiredValidCompany(Company company) throws InvalidLoginException {
		if (company == null) {
			throw new InvalidLoginException("Email or password are invalid.");
		}
	}

	private ClientSession adminLogin(String email, String password) throws InvalidLoginException {

		String adminName = "admin";
		String adminPass = "1234";
		if (password.equals(adminPass) && email.equals(adminName)) {

			AdminServiceImpl service = context.getBean(AdminServiceImpl.class);
			ClientSession session = context.getBean(ClientSession.class);
			session.setService(service);
			session.accessed();

			return session;

		}
		
		throw new InvalidLoginException("Name or password are invalid.");
	}

	@PostConstruct
	public void OnPostContruct() {

		new Thread(couponCleaner).start();
		new Thread(sessionCleaner).start();
	}

	@PreDestroy
	public void OnPreDestroy() {
		sessionCleaner.stop();
		couponCleaner.stop();
	}
}
