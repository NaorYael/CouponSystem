package com.coupons.rest.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coupons.rest.ClientSession;
import com.coupons.rest.CouponSystem;
import com.coupons.rest.LoginType;
import com.coupons.rest.controller.ex.InvalidLoginException;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api")
public class LoginController {

	private static final int LENGTH_TOKEN = 15;
	private CouponSystem couponSystem;
	private Map<String, ClientSession> tokensMap;

	@Autowired
	public LoginController(CouponSystem couponSystem,
			@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.couponSystem = couponSystem;
		this.tokensMap = tokensMap;
	}
/**
 * This function 
 * @param email - the given email.
 * @param password - the given password.
 * @param type - the given type.
 * @return - token? service?
 * @throws InvalidLoginException
 */
	@PostMapping("/login")
	public ResponseEntity<String> login(
			@RequestParam String email, @RequestParam String password,
			@RequestParam LoginType type) throws InvalidLoginException {
		
		// check if there is a user inside the DB and return the user. (this happening by hibernate)
		ClientSession clientSession = couponSystem.login(email, password, type);
		// Creating a generate token.
		String token = generateToken();
		// Storage the session and token inside 'tokensMap'.
		tokensMap.put(token, clientSession);
		// return the token back to the user/system.
		return ResponseEntity.ok(token);
	}

	private String generateToken() {
		return UUID.randomUUID()
				.toString()
				.replaceAll("-", "")
				.substring(0, LENGTH_TOKEN);
	}

}
