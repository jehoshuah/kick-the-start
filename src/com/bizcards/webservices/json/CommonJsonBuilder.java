package com.bizcards.webservices.json;

import java.util.logging.Logger;

import com.google.gson.Gson;

public class CommonJsonBuilder {
	public static <T> T getEntityForJson(String json, Class<T> entity) {
		if(json == null)
			return null;
		try {
			return new Gson().fromJson(json, entity);
		}
		catch (Exception e) {
			//log here
			Logger log = Logger.getLogger("CommonJsonBuilder");
			log.info(e.getMessage());
			
		}
		return null;
	}

	public static String getJsonForEntity(JsonInterface entity) {
		try {
			return new Gson().toJson(entity);
		}
		catch (Exception e) {
			//log here
		}
		return null;
	}
	public static String getJsonForEntity(Object entity) {
		try {
			return new Gson().toJson(entity);
		}
		catch (Exception e) {
			//log here
		}
		return null;
	}
}
