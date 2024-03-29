package com.bizcards.webservices.database.model;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;

@Cached
public class Card
{
    @Id
    public String id;

    public String bizCardCode;
    @Indexed
	public String userFullName;
    public String companyName;
	public String designation;
	public String fax;
	
	public String imageUrl;
	public String logoImage;
	public String phone;
	public String website;
	public String email;
	
	public boolean isPrimary;
	public boolean isActive;
	public boolean isArchived;
	
	public boolean isDeleted;

}
