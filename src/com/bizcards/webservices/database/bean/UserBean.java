package com.bizcards.webservices.database.bean;

import com.bizcards.webservices.json.JsonInterface;


public class UserBean implements JsonInterface<Object>{

	public String id;
	public String username;
    public String bizCardCode;
	public String password;
	public String name;
	public String email;
	public String phone;
	public String imageUrl;
	public String devicePushNotificationId;
	public String subscriptionType;// see SubscriptionType

	public boolean isDeleted;
}
