package com.bizcards.webservices.database.model;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;

@Cached
public class Contact {
	
	    @Id
	    public String id;
//	    @Indexed
//		public String userId;
	    public String bizCardCode;
	    @Indexed
		public String cardId;
	    public String type;// see ContactType
	    
	    public String notes;
	    public String imageUrl;
		public boolean isDeleted;

	}
