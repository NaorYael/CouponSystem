package com.coupons.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SessionCleaner implements Runnable {

	private static final long MIN_LENGTH_MILLIS = (30) * (60_000); // 30 minutes.
	private Map<String, ClientSession> tokensMap;
	private Map<String, ClientSession> tokensMapCopy;
	private boolean isWorking;

	@Autowired
	public SessionCleaner(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.tokensMap = tokensMap;
		this.tokensMapCopy = new HashMap<>();
	}

	@Override
	public void run() {
		isWorking = true;
		while (isWorking) {
			// copy the origin map to copyMap.
			tokensMapCopy.putAll(tokensMap);
			// while the map size equal run.
			while (tokensMapCopy.size() == tokensMap.size()) {
				for (Iterator<Map.Entry<
						String, ClientSession>> entryIt = tokensMapCopy.entrySet().iterator(); entryIt
						.hasNext();) {
					Map.Entry<String, ClientSession> entry = entryIt.next();
					long lastAccessedMillis = entry.getValue().getLastAccessedMillis();
					if (System.currentTimeMillis() - lastAccessedMillis >= MIN_LENGTH_MILLIS) {
						tokensMap.remove(entry.getKey());
//						System.out.println(String.format("token remove - %s", entry.getKey()));
						entryIt.remove();
					}
				}
			}
		}

	}

	public void stop() {
		isWorking = false;
	}

}
