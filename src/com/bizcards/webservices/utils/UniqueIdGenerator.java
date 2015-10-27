package com.bizcards.webservices.utils;

import java.util.Date;

public class UniqueIdGenerator {

	private static UniqueIdGenerator instance;
	
	public static UniqueIdGenerator getInstance() {
		if(instance == null) instance = new UniqueIdGenerator();
		return instance;
	}
	
	@SuppressWarnings("static-access")
	public String getId(){
		try {
			Thread.currentThread().sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Long.toString(new Date().getTime());
	}
	
	public String getCurrentTime(){
		return Long.toString(new Date().getTime());
	}
	
	public String getTimeAfter(int minutes){
		return Long.toString(new Date().getTime() + minutes * 60000);
	}
	
	public String getAuthToken(){
		return EncryptDecrypt.getSha2Hash(getCurrentTime());
	}
	
}
