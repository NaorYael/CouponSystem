package com.coupons.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coupons.entity.Coupon;
import com.coupons.repository.CouponRepository;

@Service
public class CouponCleaner implements Runnable {

	private CouponRepository repository;
	private boolean isWorking;
	private long lastCleanedMillis;
	private static final long DAY_LENGTH_MILLIS = 86_400_000; // 24 hours.

	@Autowired
	public CouponCleaner(CouponRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run() {
		isWorking = true;
		while (isWorking) {
			if (System.currentTimeMillis() - lastCleanedMillis >= DAY_LENGTH_MILLIS) {
				lastCleanedMillis = System.currentTimeMillis();
				List<Coupon> coupons = repository.findAllByEndDateBefore(new Date());
				if (!coupons.isEmpty()) {
					repository.deleteAll(coupons);
				}

			}
		}

	}

	public void stop() {
		isWorking = false;
	}
}
