package com.bizcards.webservices.resource;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.ServerResponse;
import com.bizcards.webservices.database.bean.CardBean;
import com.bizcards.webservices.database.bean.CardShareBean;
import com.bizcards.webservices.database.dao.CardDao;
import com.bizcards.webservices.database.dao.CardShareDao;
import com.bizcards.webservices.database.dao.UserDao;
import com.bizcards.webservices.database.model.Card;
import com.bizcards.webservices.database.model.CardShare;
import com.bizcards.webservices.database.model.User;
import com.bizcards.webservices.gcm.PNManager;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.bizcards.webservices.utils.Constants;
import com.bizcards.webservices.utils.DateTimeConverter;



@Path("/card")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardResource extends BaseResource{
	
	@GET
	@Path("/get")
	public String getCard(@QueryParam("card_id") String cardId) {
		CardBean cardBean = BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(cardId));
		
		if(cardBean == null)
			return getNoResultsServerResponse();
		else 
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true, "Fetched Card Bean", Status.OK.getStatusCode(), cardBean));
	}
	
	@GET
	@Path("/get-all")
	public String getAllCards() {
		List<CardBean> cardBeans = new ArrayList<CardBean>();
		cardBeans = CardDao.getInstance().getAllCards();
		
		if(cardBeans == null || cardBeans.isEmpty())
			return getNoResultsServerResponse();
		else 
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<List<CardBean>>(true, "Fetched Card Beans", Status.OK.getStatusCode(), cardBeans));
	}
	
	@GET
	@Path("/get-user-cards")
	public String getUserCards(@Context HttpServletRequest hh) {
		String userId = hh.getAttribute(Constants.USER_ID).toString();
		User user = UserDao.getInstance().getRecordWithId(userId);
		
		List<CardBean> cardBeans = new ArrayList<CardBean>();
		cardBeans = CardDao.getInstance().getCardBeansWithBizCardCode(user.bizCardCode);
		
		if(cardBeans == null || cardBeans.isEmpty())
			return getNoResultsServerResponse();
		else 
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<List<CardBean>>(true, "Fetched Card Beans of the User", Status.OK.getStatusCode(), cardBeans));
	}
	
	@GET
	@Path("/get-shared-cards")
	public String getSharedCards(@Context HttpServletRequest hh) {
		String userId = hh.getAttribute(Constants.USER_ID).toString();

		List<CardBean> cardBeans = new ArrayList<CardBean>();
		cardBeans = CardShareDao.getInstance().getSharedCardsToUserId(userId);
		
		if(cardBeans == null || cardBeans.isEmpty())
			return getNoResultsServerResponse();
		else 
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<List<CardBean>>(true, "Fetched Card Beans of the User", Status.OK.getStatusCode(), cardBeans));
	}
	
	@POST
	@Path("/add-edit")
	public String addEditCard(@Context HttpServletRequest hh, String data){
		String userId = hh.getAttribute(Constants.USER_ID).toString();

		CardBean bean = CommonJsonBuilder.getEntityForJson(data, CardBean.class);
		
		Card card;
		if(bean.id == null){

			bean.bizCardCode = UserDao.getInstance().getBizCardCodeWithId(userId);
			
			card = CardDao.getInstance().add(bean);

			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true, "Succesfully Added Card", Status.OK.getStatusCode(), BeanConverter.getInstance().getCardBean(card)));
		} else {
			//update record
			card = BeanConverter.getInstance().getCard(bean);
			Card original = CardDao.getInstance().getRecord(bean.id);

			//cant change these during edit
			card.isActive = original.isActive;
			card.isArchived = original.isArchived;
			card.isPrimary = original.isPrimary;
			
			card = CardDao.getInstance().update(card.id, card);
			
			boolean result = false;
			if (card.isActive) 
				result = PNManager.getInstance().notifyCardUpdated(card.id, userId);
			
			if(!result)
				return getUnSuccesfullPushNotificationResponse();
			
			CardBean cardBean = BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(card.id));
	
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true, "Succesfully Updated Card", Status.OK.getStatusCode(), cardBean));
		}
	}
	
	@POST
	@Path("/share")
	public String sendShareRequest(@Context HttpServletRequest hh, @QueryParam("biz-card-code") String bizCardCode, @QueryParam("card_id") String cardId ){
				
		String senderId = hh.getAttribute(Constants.USER_ID).toString();

		if(CardDao.getInstance().getRecord(cardId) == null)
			return getErrorResponse(String.format("No Card found with Id %s", cardId), Status.NO_CONTENT.getStatusCode());
		
		if (!User.validateBizCardCode(bizCardCode))
			return getInvalidBizCardCodeResponse();
		
		User receiver = UserDao.getInstance().getUserWithBizCardCode(bizCardCode);

		if(receiver == null)
			return getErrorResponse(String.format("No User found with code '%s'", bizCardCode), Status.NO_CONTENT.getStatusCode());
				
		String cardShareId = CardShareDao.getInstance().shareCardToUser(receiver.id, senderId, cardId);

		boolean result = PNManager.getInstance().notifyCardShare(senderId, receiver.id, cardId, cardShareId);
		
		if(!result)
			return getUnSuccesfullPushNotificationResponse();
		
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<Object>(false, "Request Successfully sent!", Status.OK.getStatusCode(), null));	
	}
	
	@POST
	@Path("/accept")
	public String addCardToUserWithUsername(@Context HttpServletRequest hh, @QueryParam("card_share_id") String cardShareId){
				
		CardShareBean cardShareBean = CardShareDao.getInstance().getCardShareBean(cardShareId);
		
		if(cardShareBean == null)
			return getErrorResponse(String.format("Invalid CardShareId!"), Status.BAD_REQUEST.getStatusCode());

		if(CardDao.getInstance().getRecord(cardShareBean.cardId) == null)
			return getErrorResponse(String.format("No Card found with Id %s", cardShareBean.cardId), Status.NO_CONTENT.getStatusCode());
		
		User sender = UserDao.getInstance().getRecordWithId(cardShareBean.senderId);

		if(sender == null)
			return getErrorResponse(String.format("No User found with id '%s'", cardShareBean.senderId), Status.NO_CONTENT.getStatusCode());
		
		cardShareBean.acceptedTime = DateTimeConverter.getInstance().getUTCTimeStringNow();
		cardShareBean.isActive = true;
		CardShareDao.getInstance().update(BeanConverter.getInstance().getCardShare(cardShareBean));
		
		Card card = CardDao.getInstance().getRecord(cardShareBean.cardId);
		card.isActive = true;
		CardDao.getInstance().update(card.id, card);
		boolean result = PNManager.getInstance().notifyCardAccepted(cardShareId);
		
		if(!result)
			return getUnSuccesfullPushNotificationResponse();
		
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<Object>(false, "Successfully Accepted card!", Status.OK.getStatusCode(), null));	
	}
	
	@GET
	@Path("/make-primary/{id}")
	public String makePrimary(@PathParam("id") String id) {
		
		Card card = CardDao.getInstance().getRecord(id);

		Card primaryCard = CardDao.getInstance().getPrimaryCardWithBizCardCode(card.bizCardCode);
		primaryCard.isPrimary = false;
		CardDao.getInstance().update(card.id, primaryCard);
		
		card.isPrimary = true;
		CardDao.getInstance().update(id, card);
		
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true,"Succesfully made primary!",Status.OK.getStatusCode(),
				BeanConverter.getInstance().getCardBean(card)));

	}
	
	@GET
	@Path("/archive/{id}")
	public String archive(@Context HttpServletRequest hh, @PathParam("id") String id) {
		String senderId = hh.getAttribute(Constants.USER_ID).toString();

		Card card = CardDao.getInstance().getRecord(id);
		card.isArchived = true;
		CardDao.getInstance().update(id, card);
		
		boolean result = false;
		
		if (card.isPrimary) 
			return getErrorResponse(String.format("Primary Card cannot be archived, please make another existing cards as primary!"), Status.BAD_REQUEST.getStatusCode());
		
		if (card.isActive) 
			result = PNManager.getInstance().notifyCardArchived(id, senderId);
		else
			result = true;
		
		if(!result)
			return getUnSuccesfullPushNotificationResponse();

		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true,"Succesfully archived!",Status.OK.getStatusCode(),
				BeanConverter.getInstance().getCardBean(card)));

	}
	
	@GET
	@Path("/delete/{id}")
	public String delete(@Context HttpServletRequest hh, @PathParam("id") String id) {
		
		Card card = CardDao.getInstance().getRecord(id);
		//own card
		String userId = hh.getAttribute(Constants.USER_ID).toString();
		User user = UserDao.getInstance().getRecordWithId(userId);
		if (user.bizCardCode.equalsIgnoreCase(card.bizCardCode))
			CardDao.getInstance().delete(id);
		else {
			CardShare cardShare = CardShareDao.getInstance().getCardShareWithReceiverIdAndCardId(userId, card.id);
			CardShareDao.getInstance().deletePermanently(cardShare.id);
		}
		//shared card
		Card deleted = CardDao.getInstance().delete(id);

		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true,"Succesfully deleted Card",Status.OK.getStatusCode(),
				BeanConverter.getInstance().getCardBean(deleted)));

	}
	
//	@GET
//	@Path("/delete-permenant/{id}")
//	public String deletePermanent(@PathParam("id") String id) {
//		
//		Card deleted = CardDao.getInstance().deletePermanently(id);
//		
//		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true,"Succesfully deleted Card",Status.OK.getStatusCode(),
//				BeanConverter.getInstance().getCardBean(deleted)));
//	}
}