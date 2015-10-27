package com.bizcards.webservices.utils;

import java.util.logging.Logger;

public class MyLog {

	private static Logger mylog;
	
	static {
		synchronized (MyLog.class) {
			if (mylog == null)
				mylog = Logger.getLogger("org.lefi.beautifulbooks");
		}
	}

	public static void startMethod(String className, String methodName){
		mylog.info("CLASS: " + className + " : " + methodName + " => START");
	}
	public static void endMethod(String className, String methodName){
		mylog.info("CLASS: " + className + " : " + methodName + " => END");
	}
	public static void catchError(String className, String methodName, String errorTxt) {
		mylog.severe("CLASS: " + className + " : " + methodName + " => Error: " + errorTxt);
	}
	public static void write(String className, String methodName, String log){
		mylog.info("CLASS: " + className + " :: " + methodName + " :: " + log);
	}
	public static void quick(String msg){
		mylog.info(msg);
	}
	/*public static void toWrite(String className, String methodName, String log){
		mylog.info("CLASS: " + className + " :: " + methodName + " :: " + log);
	}
	public static void justWrite(String content) {
		mylog.info(content);
	}*/
	
}
