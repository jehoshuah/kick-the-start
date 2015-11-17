package com.bizcards.webservices.database;

import com.bizcards.webservices.database.bean.CardBean;
import com.bizcards.webservices.database.bean.CardShareBean;
import com.bizcards.webservices.database.bean.ContactBean;
import com.bizcards.webservices.database.bean.UserBean;
import com.bizcards.webservices.database.dao.CardDao;
import com.bizcards.webservices.database.model.Card;
import com.bizcards.webservices.database.model.CardShare;
import com.bizcards.webservices.database.model.Contact;
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
		bean.id = model.id;
		bean.receiverId = model.receiverId;
		bean.senderId = model.senderId;
		bean.sentTime = DateTimeConverter.getInstance().getUTCDateString(model.sentTime);
		bean.acceptedTime = DateTimeConverter.getInstance().getUTCDateString(model.acceptedTime);
		bean.cardId = model.cardId;
		bean.isActive = model.isActive;
		
		return bean;
	}
	
	public CardShare getCardShare(CardShareBean bean) {
		
		CardShare model = new CardShare();
		model.id = bean.id;
		model.receiverId = bean.receiverId;
		model.senderId = bean.senderId;
		model.sentTime = DateTimeConverter.getInstance().getUTCDate(bean.sentTime);
		model.acceptedTime = DateTimeConverter.getInstance().getUTCDate(bean.acceptedTime);
		model.cardId = bean.cardId;
		model.isActive = bean.isActive;
		
		return model;
	}
	
	public CardBean getCardBean(Card model) {
		CardBean bean = new CardBean();

		bean.id = model.id;
		bean.bizCardCode = model.bizCardCode;
		bean.companyName = model.companyName;
		bean.userFullName = model.userFullName;
		bean.designation = model.designation;
		bean.fax = model.fax;
		bean.phone = model.phone;
		bean.website = model.website;
		bean.email = model.email;
		
		bean.imageUrl = StoragePathHelper.getResourceUrlGCS(model.imageUrl);
		bean.logoImage = StoragePathHelper.getResourceUrlGCS(model.logoImage);
		
		bean.isActive = model.isActive;
		bean.isArchived = model.isArchived;
		bean.isPrimary = model.isPrimary;
		
		bean.isDeleted = model.isDeleted;
		
		return bean;
	}


	public Card getCard(CardBean bean) {
		Card model = new Card();
		
		model.id = bean.id;
		model.bizCardCode = bean.bizCardCode;
		model.companyName = bean.companyName;
		model.userFullName = bean.userFullName;
		model.designation = bean.designation;
		model.fax = bean.fax;
		model.phone = bean.phone;
		model.website = bean.website;
		model.email = bean.email;
		
		model.imageUrl = bean.imageUrl;
		model.logoImage = bean.logoImage;
		
		model.isActive = bean.isActive;
		model.isArchived = bean.isArchived;
		model.isPrimary = bean.isPrimary;
		
		model.isDeleted = bean.isDeleted;
		
		return model;
	}

	public ContactBean getContactBean(Contact model) {
		ContactBean bean = new ContactBean();

		bean.id = model.id;
		bean.bizCardCode = model.bizCardCode;
		bean.cardId = model.cardId;
		bean.cardBean = BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(bean.cardId));
		bean.type = model.type;
		bean.notes = model.notes;
		bean.imageUrl = StoragePathHelper.getResourceUrlGCS(model.imageUrl);
		bean.isDeleted = model.isDeleted;
		
		return bean;
	}


	public Contact getContact(ContactBean bean) {
		Contact model = new Contact();
		
		model.id = bean.id;
		model.bizCardCode = bean.bizCardCode;
		model.cardId = bean.cardId;
		model.type = bean.type;
		model.notes = bean.notes;
		model.imageUrl = bean.imageUrl;
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
		bean.bizCardCode = model.bizCardCode;
		bean.password = model.password;
		bean.imageUrl = StoragePathHelper.getResourceUrlGCS(model.imageUrl);
		bean.subscriptionType = model.subscriptionType;
		
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
		model.bizCardCode = bean.bizCardCode;
		model.password = bean.password;
		model.imageUrl = bean.imageUrl;
		model.subscriptionType = bean.subscriptionType;
		
		model.isDeleted = bean.isDeleted;
		
		return model;
	}

}
