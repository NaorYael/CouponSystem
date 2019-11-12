package com.coupons.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.coupons.rest.controller.AdminController;
import com.coupons.rest.controller.CompanyController;
import com.coupons.rest.controller.CustomerController;
import com.coupons.rest.controller.LoginController;
import com.coupons.rest.controller.ex.AlreadyPurchaseCouponException;
import com.coupons.rest.controller.ex.UnableToChangeEmailException;
import com.coupons.rest.controller.ex.EmailAlreadyExistException;
import com.coupons.rest.controller.ex.FaildCreateObjectException;
import com.coupons.rest.controller.ex.IllegalCouponException;
import com.coupons.rest.controller.ex.InvalidLoginException;
import com.coupons.rest.controller.ex.NoSuchCompanyException;
import com.coupons.rest.controller.ex.NoSuchCouponException;
import com.coupons.rest.controller.ex.NoSuchCustomerException;
import com.coupons.rest.controller.ex.TaskMalformedException;
import com.coupons.rest.controller.ex.TitleAlreadyExistException;
import com.coupons.rest.controller.ex.TitleOrDateMissingException;

@ControllerAdvice(assignableTypes = { 
		LoginController.class, CompanyController.class, CustomerController.class, AdminController.class })
public class CouponSystemRestExceptionHandler {
	
	@ExceptionHandler(InvalidLoginException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public CouponSystemErrorResponse handleUnauthorized(InvalidLoginException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.UNAUTHORIZED, String.format("Unauthorized %s", ex.getMessage()));
	}
	
	@ExceptionHandler(UnableToChangeEmailException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public CouponSystemErrorResponse unableToChangeEmail(UnableToChangeEmailException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.FORBIDDEN, String.format("Unable To Change Email: %s", ex.getMessage()));
	}
	
	@ExceptionHandler(TitleAlreadyExistException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public CouponSystemErrorResponse unableToChangeTitle(TitleAlreadyExistException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.FORBIDDEN, String.format("Unable To Change Title: %s", ex.getMessage()));
	}
	
	@ExceptionHandler(IllegalCouponException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public CouponSystemErrorResponse handleIllegalCoupon(IllegalCouponException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.FORBIDDEN, String.format("Please Try Again: %s", ex.getMessage()));
	}
	
	@ExceptionHandler(AlreadyPurchaseCouponException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public CouponSystemErrorResponse unableToPurchaseCoupon(AlreadyPurchaseCouponException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.BAD_REQUEST, String.format("The %s is already purshace. please try again", ex.getMessage()));
	}
	
	@ExceptionHandler(EmailAlreadyExistException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public CouponSystemErrorResponse handleEmailAlreadyExist(EmailAlreadyExistException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.CONFLICT, String.format("Already Exist: %s ", ex.getMessage()));
	}
	
	@ExceptionHandler(TaskMalformedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public CouponSystemErrorResponse handleTaskMalformed(TaskMalformedException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.BAD_REQUEST, String.format("Task Malformed %s", ex.getMessage()));
	}
	
	@ExceptionHandler(NoSuchCouponException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public CouponSystemErrorResponse handleCouponNotFound(NoSuchCouponException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.NOT_FOUND, String.format("Not Found: %s", ex.getMessage()));
	}
	
	@ExceptionHandler(NoSuchCustomerException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public CouponSystemErrorResponse handleCustomerNotFound(NoSuchCustomerException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.NOT_FOUND, String.format("Not Found: %s", ex.getMessage()));
	}
	
	@ExceptionHandler(NoSuchCompanyException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public CouponSystemErrorResponse handleCompanyNotFound(NoSuchCompanyException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.NOT_FOUND, String.format("Not Found: %s", ex.getMessage()));
	}
	
	@ExceptionHandler(FaildCreateObjectException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	public CouponSystemErrorResponse unableCreateAnObject(FaildCreateObjectException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.NOT_ACCEPTABLE, String.format("Problem To Create: %s", ex.getMessage()));
	}
	
	@ExceptionHandler(TitleOrDateMissingException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	public CouponSystemErrorResponse handleTitleOrDateMissing(TitleOrDateMissingException ex) {
		return CouponSystemErrorResponse.now(HttpStatus.NOT_ACCEPTABLE, String.format("Problem To Create: %s", ex.getMessage()));
	}
}
