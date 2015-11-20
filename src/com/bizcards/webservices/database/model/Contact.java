package com.bizcards.webservices.database.model;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;

@Cached
public class Contact {
	
	    @Id
	    public String id;
	    public String bizCardCode;
	    public String type;// see ContactType
	    
	    public String notes;
	    public String imageUrl;
		public boolean isDeleted;

	}
