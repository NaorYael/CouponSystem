
package com.coupons.rest.controller.ex;

@SuppressWarnings("serial")
public class TitleAlreadyExistException extends Exception {
	public TitleAlreadyExistException(String msg) {
		super(msg);
	}
}
