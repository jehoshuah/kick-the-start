package com.bizcards.webservices.database;

import com.bizcards.webservices.database.bean.CardBean;
import com.bizcards.webservices.database.bean.CardShareBean;
import com.bizcards.webservices.database.bean.UserBean;
import com.bizcards.webservices.database.model.Card;
import com.bizcards.webservices.database.model.CardShare;
import com.bizcards.webservices.database.model.User;
import com.bizcards.webservices.utils.DateTimeConverter;
import com.bizcards.webservices.utils.StoragePathHelper;


public class BeanConverter {
	//test commit
	private static BeanConverter instance;
	public static BeanConverter getInstance(){
        if(instance == null) 
        	instance = new BeanConverter();
        return instance;
    }
	
	public CardShareBean getCardShareBean(CardShare model){
		CardShareBean bean = new CardShareBean();
		bean.receiverId = model.receiverId;
		bean.senderId = model.senderId;
		bean.sentTime = DateTimeConverter.getInstance().getUTCDateString(model.sentTime);
		bean.acceptedTime = DateTimeConverter.getInstance().getUTCDateString(model.acceptedTime);
		bean.cardId = model.cardId;
		
		return bean;
	}
	
	public CardShare getCardShare(CardShareBean bean) {
		
		CardShare model = new CardShare();
		model.receiverId = bean.receiverId;
		model.senderId = bean.senderId;
		model.sentTime = DateTimeConverter.getInstance().getUTCDate(bean.sentTime);
		model.acceptedTime = DateTimeConverter.getInstance().getUTCDate(bean.acceptedTime);
		model.cardId = bean.cardId;
		
		return model;
	}
	
	public CardBean getCardBean(Card model) {
		CardBean bean = new CardBean();

		bean.id = model.id;
		bean.userId = model.userId;
		bean.userFullName = model.userFullName;
		bean.designation = model.designation;
		bean.fax = model.fax;
		bean.phone = model.phone;
		bean.website = model.website;
		
		bean.imageUrl = StoragePathHelper.getResourceUrlGCS(model.imageUrl);
		bean.logoImage = StoragePathHelper.getResourceUrlGCS(model.logoImage);
		
		bean.isDeleted = model.isDeleted;
		
		return bean;
	}


	public Card getCard(CardBean bean) {
		Card model = new Card();
		
		model.id = bean.id;
		model.userId = bean.userId;
		model.userFullName = bean.userFullName;
		model.designation = bean.designation;
		model.fax = bean.fax;
		model.phone = bean.phone;
		model.website = bean.website;
		
		model.imageUrl = bean.imageUrl;
		model.logoImage = bean.logoImage;
		
		model.isDeleted = bean.isDeleted;
		
		return model;
	}


	public UserBean getUserBean(User model) {
		UserBean bean = new UserBean();
		bean.id = model.id;
		bean.name = model.name;
		bean.email = model.email;
		bean.phone = model.phone;
		bean.username = model.username;
		bean.password = model.password;
		bean.imageUrl = StoragePathHelper.getResourceUrlGCS(model.imageUrl);
		
		bean.devicePushNotificationId = model.devicePushNotificationId;
		bean.isDeleted = model.isDeleted;
		
		return bean;
	}

	public User getUser(UserBean bean) {
		User model = new User();
		model.id = bean.id;
		model.name = bean.name;
		model.email = bean.email;
		model.phone = bean.phone;
		model.username = bean.username;
		model.password = bean.password;
		model.imageUrl = bean.imageUrl;
		
		model.devicePushNotificationId = bean.devicePushNotificationId;
		model.isDeleted = bean.isDeleted;
		
		return model;
	}

}
