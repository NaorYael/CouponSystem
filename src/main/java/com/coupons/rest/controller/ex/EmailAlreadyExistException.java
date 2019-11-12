/* Written by Naor Yael 
 26 Sep 2019  
13:14:54*/

package com.coupons.rest.controller.ex;

@SuppressWarnings("serial")
public class EmailAlreadyExistException extends Exception {
	public EmailAlreadyExistException(String msg) {
		super(msg);
	}
}
