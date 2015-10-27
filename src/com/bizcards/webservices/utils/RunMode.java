package com.bizcards.webservices.utils;

public class RunMode {

    private static String production = "PRODUCTION";
    private static String demo = "DEMO";
    private static String qa = "QA";
    private static String stage = "STAGE";
    private static String dev = "DEV";

    //TODO: change mode before Building
    private static String currentMode = production;


    public static boolean isProduction() {
        return currentMode.equalsIgnoreCase(production);
    }

    public static boolean isDemo() {
        return currentMode.equalsIgnoreCase(demo);
    }
    
    public static boolean isQa() {
    	return currentMode.equalsIgnoreCase(qa);
    }
    
    public static boolean isStage() {
    	return currentMode.equalsIgnoreCase(stage);
    }
    
    public static boolean isDev() {
        return currentMode.equalsIgnoreCase(dev);
    }
}