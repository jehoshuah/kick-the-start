/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcards.webservices.database.bean;

import com.bizcards.webservices.json.JsonInterface;

public class CardBean implements JsonInterface<Object> {

	public String id;

	public String userFullName;
	public String imageUrl;
//	public String userId;
    public String bizCardCode;

	public String companyName;
	public String designation;

	public String logoImage;
	public String fax;

	public String phone;
	public String website;
	public String email;
	
	public boolean isPrimary;
	public boolean isActive;
	public boolean isArchived;
	
	public boolean isDeleted;

}