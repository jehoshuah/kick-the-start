package com.bizcards.webservices.database.model;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;

@Cached
public class ContactAdd {
		
		    @Id
		    public String id;
		    public String contactBizCardCode;
		    public String userBizCardCode;
		    
		    public boolean isActive;
			public boolean isDeleted;

		}
