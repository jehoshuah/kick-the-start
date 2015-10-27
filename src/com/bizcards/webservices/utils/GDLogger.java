package com.bizcards.webservices.utils;

import java.util.logging.Logger;

public class GDLogger {
	public static void logInfo(String className, String message){
		Logger log = Logger.getLogger(className);
		log.info(message);
	}
	public static void logWarning(String className, String message){
		Logger log = Logger.getLogger(className);
		log.warning(message);
	}
	public static void logSevere(String className, String message){
		Logger log = Logger.getLogger(className);
		log.severe(message);
	}
}
