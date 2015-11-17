package com.bizcards.webservices.database.model;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;

@Cached
public class User
{
    @Indexed @Id 
    public String id;
    @Indexed
    public String username;
    @Indexed
    public String bizCardCode;
    public String name;
    public String password;
	public String email;
	public String phone;
	public String imageUrl;
	
	public String subscriptionType;// see SubscriptionType
	public boolean isDeleted;
}
