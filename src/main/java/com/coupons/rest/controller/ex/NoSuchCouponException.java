
package com.coupons.rest.controller.ex;

@SuppressWarnings("serial")
public class NoSuchCouponException extends Exception{
	public NoSuchCouponException(String msg) {
		super(msg);
	}
}
