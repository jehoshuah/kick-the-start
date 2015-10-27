package com.bizcards.webservices.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Coder {
	public static String encode(String str){
		
		String encodedString = "";
		byte [] encodedBytes = Base64.encodeBase64(str.getBytes());
		encodedString = new String(encodedBytes);
		return encodedString;
	}
	public static String decode(String str){
		
		String decodedString = "";
		byte [] decodedBytes = Base64.decodeBase64(str.getBytes());
		decodedString = new String(decodedBytes);
		return decodedString;
	}
}
