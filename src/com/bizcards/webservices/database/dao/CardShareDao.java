package com.bizcards.webservices.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.bean.CardBean;
import com.bizcards.webservices.database.bean.CardShareBean;
import com.bizcards.webservices.database.bean.UserBean;
import com.bizcards.webservices.database.model.CardShare;
import com.bizcards.webservices.utils.DateTimeConverter;
import com.bizcards.webservices.utils.UniqueIdGenerator;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class CardShareDao {
	private static Objectify ofy;
	private static CardShareDao instance;
	
	static {
		ObjectifyService.register(CardShare.class);
	}
	
	public static CardShareDao getInstance() {
		ObjectifyOpts opts = new ObjectifyOpts().setSessionCache(true);
		ofy = ObjectifyService.begin(opts);
		if (instance == null) instance = new CardShareDao();
		return instance; 
	}
	
	public boolean update(CardShare cardShare){
		ofy.put(cardShare);
		return true;
	}
	
	public String shareCardToUser(String receiverId, String senderId, String cardId){
		
		CardShare map = new CardShare();
		map.id = UniqueIdGenerator.getInstance().getId();
		map.receiverId = receiverId;
		map.senderId = senderId;
		map.cardId = cardId;
		map.sentTime = DateTimeConverter.getInstance().getUTCTimeNow();
		map.isActive = false;
		ofy.put(map);
		
		return map.id;
	}
	
	public CardShareBean getCardShareBean(String id) {
		CardShare cardShare = ofy.query(CardShare.class).filter("id", id).get();
		
		return BeanConverter.getInstance().getCardShareBean(cardShare);
	}
	
	public List<CardBean> getSharedCardsFromUserId(String userId) {
		List<CardBean> cardBeans = new ArrayList<CardBean>();
		Query<CardShare> query = ofy.query(CardShare.class).filter("senderId", userId).filter("isActive", true);
		
		for (CardShare map: query) {
			cardBeans.add(BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(map.senderId)));
		}
		return cardBeans;
	}
	

	public List<CardBean> getSharedCardsToUserId(String userId) {
		List<CardBean> cardBeans = new ArrayList<CardBean>();
		Query<CardShare> query = ofy.query(CardShare.class).filter("receiverId", userId).filter("isActive", true);
		
		for (CardShare map: query) {
			cardBeans.add(BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(map.cardId)));
		}
		return cardBeans;
	}
	
	public List<UserBean> getSendersWithCardId(String userId, String cardId) {
		List<UserBean> userBeans = new ArrayList<UserBean>();
		Query<CardShare> query = ofy.query(CardShare.class)
				.filter("receiverId", userId)
				.filter("cardId", cardId)
				.filter("isActive", true);
		
		for (CardShare map: query) {
			userBeans.add(BeanConverter.getInstance().getUserBean(UserDao.getInstance().getRecordWithId(map.senderId)));
		}
		return userBeans;
	}
	
	public List<UserBean> getReceiversWithCardId(String userId, String cardId) {
		List<UserBean> userBeans = new ArrayList<UserBean>();
		Query<CardShare> query = ofy.query(CardShare.class)
				.filter("senderId", userId)
				.filter("cardId", cardId)
				.filter("isActive", true);
		
		for (CardShare map: query) {
			if (map.acceptedTime != null)
				userBeans.add(BeanConverter.getInstance().getUserBean(UserDao.getInstance().getRecordWithId(map.receiverId)));
		}
		return userBeans;
	}
	
	public CardShare getCardShareWithReceiverIdAndCardId(String receiverId, String cardId){
		return ofy.query(CardShare.class)
				.filter("receiverId", receiverId)
				.filter("cardId", cardId).get();
		}
	
	
	public CardShare deletePermanently(String id) {
		CardShare fetchedObject = ofy.query(CardShare.class).filter("id", id).get();
		ofy.delete(fetchedObject);
		return fetchedObject;
	}
}
