package com.bizcards.webservices.database;

import java.util.Date;
import java.util.List;

import com.bizcards.webservices.database.model.Card;
import com.bizcards.webservices.database.model.CardShare;
import com.bizcards.webservices.database.model.User;


public class UpdateConverter {

	private static UpdateConverter instance;
	public static UpdateConverter getInstance(){
        if(instance == null) 
        	instance = new UpdateConverter();
        return instance;
    }
	
	public Card getUpdatedCard(Card original, Card updated) {
	
		if(updateRequired(updated.userFullName))	original.userFullName = updated.userFullName;
		if(updateRequired(updated.designation))	original.designation = updated.designation;
		
		if(updateRequired(updated.imageUrl))	original.imageUrl = updated.imageUrl;
		if(updateRequired(updated.logoImage))	original.logoImage = updated.logoImage;
		if(updateRequired(updated.website))	original.website = updated.website;
		if(updateRequired(updated.fax))	original.fax = updated.fax;
		if(updateRequired(updated.userId))	original.userId = updated.userId;

		return original;
	}

	public User getUpdatedUser(User original, User updated) {

		if(updateRequired(updated.name))	 original.name = updated.name;
		if(updateRequired(updated.phone))	 original.phone = updated.phone;
		if(updateRequired(updated.email))	 original.email = updated.email;
		if(updateRequired(updated.imageUrl))	 original.imageUrl = updated.imageUrl;
		if(updateRequired(updated.devicePushNotificationId)) original.devicePushNotificationId = updated.devicePushNotificationId;
		
		return original;
	}
	
	public CardShare getUpdatedCardShare(CardShare original, CardShare updated) {		
	
		return original;
	}
	
	private boolean updateRequired(Date date){
		return date != null ? true : false;
	}
	
	private boolean updateRequired(List<String> updated){
		return updated != null && !updated.isEmpty() ? true : false;
	}
	
	private boolean updateRequired(String updated){
		return updated != null && !updated.isEmpty() ? true : false;
	}
	
	private boolean updateRequired(int updated){
		return updated != 0 ? true : false;
	}
	
	private boolean updateRequired(float updated){
		return updated != 0.0 ? true : false;
	}

	private boolean updateRequired(boolean original, boolean updated){
		return original != updated ? true : false;
	}

}
