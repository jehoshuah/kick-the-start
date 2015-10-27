package com.bizcards.webservices.utils;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;


public class AuthHelper {
	public static String ACCESS_ID_HEADER_TAG = "accessid";
    public static String NONCE_HEADER_TAG = "nonce";
    public static String ACCESS_KEY_HEADER_TAG = "accesskey";
    
	public boolean authenticate(HttpHeaders hh) {
		String accessId = getAccessId(hh);
		if(accessId == null){
			//no accessId header
			return false;
		}
		String password = "dummy";//getPasswordForUsername(accessId);
		String accessKey = getAccessKey(hh);
		if(accessKey == null){
			//no accessKey header
			return false;
		}
		return accessKey.equals(password) ? true : false;
	}
	/*private String getPasswordForUsername(String username) {
		User user = UserDao.getInstance().getUserWithUsername(username);
		return user.password;
	}*/
	private String getAccessId(HttpHeaders hh){
		List<String> header = hh.getRequestHeader(ACCESS_ID_HEADER_TAG);
		String result = null;
		if(header != null && header.get(0) != null){
			result = header.get(0);
			return result;
		}
		return result;
	}
	private String getAccessKey(HttpHeaders hh){
		List<String> header = hh.getRequestHeader(ACCESS_KEY_HEADER_TAG);
		String result = null;
		if(header != null && header.get(0) != null){
			result = header.get(0);
			return result;
		}
		return result;
	}
}
