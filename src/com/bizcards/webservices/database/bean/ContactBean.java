package com.bizcards.webservices.database.bean;

import com.bizcards.webservices.json.JsonInterface;

public class ContactBean  implements JsonInterface<Object> {
	
		public String id;

	    public String bizCardCode;
	    public String type;// see ContactType
	    public UserBean userBean;
	    
	    public String notes;
	    public String imageUrl;
		public boolean isDeleted;

	}