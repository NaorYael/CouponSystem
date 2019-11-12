
package com.coupons.rest.controller.ex;

@SuppressWarnings("serial")
public class NoSuchCustomerException extends Exception{
	public NoSuchCustomerException(String msg) {
		super(msg);
	}
}
