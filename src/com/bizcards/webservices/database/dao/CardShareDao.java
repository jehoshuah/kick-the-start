package com.bizcards.webservices.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.bean.CardBean;
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
		
	public boolean shareCardToUser(String receiverId, String senderId, String cardId){
		
		CardShare map = new CardShare();
		map.id = UniqueIdGenerator.getInstance().getId();
		map.receiverId = receiverId;
		map.senderId = senderId;
		map.cardId = cardId;
		map.sentTime = DateTimeConverter.getInstance().getUTCTimeNow();
		ofy.put(map);
		
		return true;
	}
	
	public List<CardBean> getSharedCardsFromUserId(String userId) {
		List<CardBean> cardBeans = new ArrayList<CardBean>();
		Query<CardShare> query = ofy.query(CardShare.class).filter("senderId", userId);
		
		for (CardShare map: query) {
			cardBeans.add(BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(map.senderId)));
		}
		return cardBeans;
	}
	

	public List<CardBean> getSharedCardsToUserId(String userId) {
		List<CardBean> cardBeans = new ArrayList<CardBean>();
		Query<CardShare> query = ofy.query(CardShare.class).filter("receiverId", userId);
		
		for (CardShare map: query) {
			cardBeans.add(BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(map.cardId)));
		}
		return cardBeans;
	}
	
	public List<UserBean> getSendersWithCardId(String userId, String cardId) {
		List<UserBean> userBeans = new ArrayList<UserBean>();
		Query<CardShare> query = ofy.query(CardShare.class)
				.filter("receiverId", userId)
				.filter("cardId", cardId);
		
		for (CardShare map: query) {
			userBeans.add(BeanConverter.getInstance().getUserBean(UserDao.getInstance().getRecordWithId(map.senderId)));
		}
		return userBeans;
	}
	
	public List<UserBean> getReceiversWithCardId(String userId, String cardId) {
		List<UserBean> userBeans = new ArrayList<UserBean>();
		Query<CardShare> query = ofy.query(CardShare.class)
				.filter("senderId", userId)
				.filter("cardId", cardId);
		
		for (CardShare map: query) {
			userBeans.add(BeanConverter.getInstance().getUserBean(UserDao.getInstance().getRecordWithId(map.receiverId)));
		}
		return userBeans;
	}
	
	public CardShare deletePermanently(String id) {
		CardShare fetchedObject = ofy.query(CardShare.class).filter("id", id).get();
		ofy.delete(fetchedObject);
		return fetchedObject;
	}
}
