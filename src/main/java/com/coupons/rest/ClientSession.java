package com.coupons.rest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.coupons.service.AbsService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClientSession {

	private AbsService service;
	private long lastAccessedMillis;

	public AbsService getService() {
		return service;
	}

	public void setService(AbsService service) {
		this.service = service;
	}

	public long getLastAccessedMillis() {
		return lastAccessedMillis;
	}

	public void accessed() {
		this.lastAccessedMillis = System.currentTimeMillis();
	}

}
