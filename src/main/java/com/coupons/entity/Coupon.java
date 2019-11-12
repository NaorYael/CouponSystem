package com.coupons.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "coupon")
public class Coupon {

	@Id
	// ?
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "title")
	private String title;
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	@Column(name = "amount")
	private int amount;
	@Column(name = "category")
	private int category;
	@Column(name = "message")
	private String message;
	@Column(name = "price")
	private double price;
	@Column(name = "image_url")
	private String imageURL;

	@ManyToOne(
			// we use that cascading because we want to add 'company_id' in the coupon column.
			cascade = { CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToMany(
			// all the cascade type instead of remove.
			cascade = { CascadeType.DETACH,
					CascadeType.MERGE,
					CascadeType.PERSIST,
					CascadeType.REFRESH })
	
	// if there is no 'customer_coupon' table create a new one.
	@JoinTable(
			name = "customer_coupon",
			joinColumns = @JoinColumn(name = "coupon_id"),
			inverseJoinColumns = @JoinColumn(name = "customer_id"))
	List<Customer> customers;

	public Coupon() {
		customers = new ArrayList<>();
	}

	public void addCustomer(Customer customer) {
		customers.add(customer);
	}

	@JsonIgnore
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@JsonIgnore
	public List<Customer> getCustomers() {
		return customers;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

}
