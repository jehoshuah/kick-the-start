package com.bizcards.webservices.utils;

public class StoragePathHelper {
	//http://commondatastorage.googleapis.com/smart-serve/images/ChristmasWallpaper.jpg
	//private String baseUrl = "http://commondatastorage.googleapis.com/smart-serve";
	private static String images = "/images";
	private static String category = "/category/";
	private static String kitchen = "/kitchen/";
	private static String restaurant = "/restaurant/";
	private static String account = "/account/";
	private static String menuDump = "/menuDump";
	private static String item = "/item/";
	private static String user = "/user/";
	private static String restaurantUser = "/restaurant-user/";
	private static String imageFileExtension = ".png";
	private static String dumpFileExtension = ".bizcards";
	private static String card = "/card/";

	private static StoragePathHelper instance;
	
	public static StoragePathHelper getInstance() {
		if (instance == null) instance = new StoragePathHelper();
		return instance; 
	}
	
	public static String getItemImagePath(String resourceId, String restaurantId, String type){
		// /images/{userId}/item/{username}_main.jpg
		return String.format("%s%s%s%s_%s%s", restaurantId, images, item, resourceId, type, imageFileExtension);
	}
	
	public static String getCategoryImagePath(String resourceId, String restaurantId, String type){
		// /images/{userId}/category/{username}.jpg
		return String.format("%s%s%s%s/%s%s", restaurantId, images, category, resourceId, type, imageFileExtension);
	}
	
	public static String getResourceUrlGCS(String relativePath){
		
		if(relativePath == null) return null;
			
		if(relativePath.startsWith("http"))
			return relativePath;
		return getResourceUrlFull(relativePath, Constants.GCS_BASE_URL);
	}
	
	public static String getResourceUrlExternal(String relativePath){
		return getResourceUrlFull(relativePath, Constants.GCS_EXTERNAL_BASE_URL);
	}
	
	private static String getResourceUrlFull(String relativePath, String baseUrl){
		return String.format("%s/%s/%s", baseUrl,Constants.getBucketName(),relativePath);
	}

	public static String getKitchenImagePath(String resourceId, String restaurantId, String type) {
		return String.format("%s%s%s/%s%s", restaurantId, images, kitchen, resourceId, type, imageFileExtension);
	}

	public static String getAccountImagePath(String resourceId, String accountId) {
		return String.format("%s%s%s%s%s", accountId, images, account, resourceId, imageFileExtension);
	}
	
	public static String getRestaurantImagePath(String resourceId, String restaurantId, String type) {
		return String.format("%s%s%s%s/%s%s", restaurantId, images, restaurant, resourceId, type, imageFileExtension);
	}
	
	public static String getCardImagePath(String resourceId) {
		return String.format("%s%s%s/%s%s", images, card, resourceId, "card-image", imageFileExtension);
	}
	
	public static String getCardLogoPath(String resourceId) {
		return String.format("%s%s%s/%s%s", images, card, resourceId, "card-logo", imageFileExtension);
	}

	public static String getUserImagePath(String resourceId, String type) {
		return String.format("%s%s%s/%s%s", images, user, resourceId, type, imageFileExtension);
	}

	public static String getRestaurantUserImagePath(String resourceId, String restaurantId, String type) {
		return String.format("%s%s%s%s/%s%s", restaurantId, images, restaurantUser, resourceId, type, imageFileExtension);
	}

	public static String getRestaurantMenuDumpPath(String restaurantId) {
		return String.format("%s%s%s", restaurantId, String.format("%s-%s", menuDump, UniqueIdGenerator.getInstance().getId()), dumpFileExtension);
	}
}
