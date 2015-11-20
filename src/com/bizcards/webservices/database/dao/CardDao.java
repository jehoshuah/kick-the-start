package com.bizcards.webservices.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.UpdateConverter;
import com.bizcards.webservices.database.bean.CardBean;
import com.bizcards.webservices.database.model.Card;
import com.bizcards.webservices.utils.UniqueIdGenerator;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class CardDao extends BaseDao implements IDao{
	private static Objectify ofy;
	private static CardDao instance;
	
	static {
		ObjectifyService.register(Card.class);		
	}

	public List<CardBean> getCardBeansWithBizCardCode(String bizCardCode){
		List<CardBean> cardBeans = new ArrayList<CardBean>();
		
		Query<Card> query = ofy.query(Card.class)
				.filter("bizCardCode", bizCardCode)
				.filter("isDeleted", false);
		if(query == null) return null;
		for(Card card : query) {
			cardBeans.add(BeanConverter.getInstance().getCardBean(card));
		}
		return cardBeans;
	}
	
	public Card getPrimaryCardWithBizCardCode(String bizCardCode){
		
		Card card = ofy.query(Card.class)
				.filter("bizCardCode", bizCardCode)
				.filter("isPrimary", true)
				.filter("isDeleted", false).get();
		
		if(card == null) return null;

		return card;
	}
	
	public List<CardBean> getAllCards(){
		List<CardBean> cardBeans = new ArrayList<CardBean>();
		
		Query<Card> query = ofy.query(Card.class)
				.filter("isDeleted", false);
		if(query == null) return null;
		for(Card card : query) {
			cardBeans.add(BeanConverter.getInstance().getCardBean(card));
		}
		return cardBeans;
	}
	
	public Card getRecord(String cardId){
		Card card = ofy.query(Card.class).filter("id", cardId).get();
		return card;
	}
	
	public Card update(String cardId, Card card){
		Card fetchedCard = ofy.query(Card.class).filter("id", cardId).get();
		fetchedCard = UpdateConverter.getInstance().getUpdatedCard(fetchedCard, card);
		ofy.put(fetchedCard);
		return fetchedCard;
	}
	
	public Card add(CardBean cardBean){
		
		Card card = BeanConverter.getInstance().getCard(cardBean);
		card.id = UniqueIdGenerator.getInstance().getId();
	
		ofy.put(card);
		
		return card;
	}
	
	public static CardDao getInstance() {
		ObjectifyOpts opts = new ObjectifyOpts().setSessionCache(true);
		ofy = ObjectifyService.begin(opts);
		if (instance == null) instance = new CardDao();
			return instance; 
	}

	public Card delete(String id) {
		Card fetchedObject = ofy.query(Card.class).filter("id", id).get();
		fetchedObject.isDeleted = true;
		ofy.put(fetchedObject);
		return fetchedObject;
	}
	
	public Card deletePermanently(String id) {
		Card fetchedObject = ofy.query(Card.class).filter("id", id).get();
		ofy.delete(fetchedObject);
		return fetchedObject;
	}
}
