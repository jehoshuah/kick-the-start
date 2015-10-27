package com.bizcards.webservices.utils;


public class Constants {

	// NOTE: Need to change this before deploy
   	public static String GCS_BUCKET_NAME_DEMO = "bizcards-prod"; 
	public static String GCS_BUCKET_NAME_PRODUCTION = "bizcards-prod"; 
	public static String GCS_BUCKET_NAME_QA = "bizcards-prod"; 
	public static String GCS_BUCKET_NAME_STAGE = "bizcards-prod"; 
	public static String GCS_BUCKET_NAME_DEV = "bizcards-prod";


   	public static String APP_ID_DEMO = "http://bizcards-prod.appspot.com"; 
	public static String APP_ID_PRODUCTION = "http://bizcards-prod.appspot.com"; 
	public static String APP_ID_QA = "http://bizcards-prod.appspot.com";
	public static String APP_ID_STAGE = "http://bizcards-prod.appspot.com"; 
	public static String APP_ID_DEV = "http://bizcards-prod.appspot.com";

	public static String getRootUrl() {
		if (RunMode.isDemo())
			return APP_ID_DEMO;
		if (RunMode.isProduction())
			return APP_ID_PRODUCTION;
		if (RunMode.isQa())
			return APP_ID_QA;
		if (RunMode.isStage())
			return APP_ID_STAGE;
		if (RunMode.isDev())
			return APP_ID_DEV;
		return APP_ID_QA;
	}
	
	public static String getBucketName() {
		if (RunMode.isDemo())
			return GCS_BUCKET_NAME_DEMO;
		if (RunMode.isProduction())
			return GCS_BUCKET_NAME_PRODUCTION;
		if (RunMode.isQa())
			return GCS_BUCKET_NAME_QA;
		if (RunMode.isStage())
			return GCS_BUCKET_NAME_STAGE;
		if (RunMode.isDev())
			return GCS_BUCKET_NAME_DEV;
		return GCS_BUCKET_NAME_QA;
	}
	
	public static String GCS_BASE_URL = "https://commondatastorage.googleapis.com";
	public static String GCS_EXTERNAL_BASE_URL = "https://commondatastorage.googleapis.com"; //Set the cname'ed URL here.
	public static String ROOT_USERNAME = "root";
	public static String USER_ID = "USER_ID";

}
