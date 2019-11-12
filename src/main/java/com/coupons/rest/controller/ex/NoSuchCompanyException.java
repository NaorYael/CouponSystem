
package com.coupons.rest.controller.ex;

@SuppressWarnings("serial")
public class NoSuchCompanyException extends Exception{
	public NoSuchCompanyException(String msg) {
		super(msg);
	}
}