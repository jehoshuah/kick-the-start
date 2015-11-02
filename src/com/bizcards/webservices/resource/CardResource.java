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
import com.bizcards.webservices.database.dao.CardDao;
import com.bizcards.webservices.database.dao.CardShareDao;
import com.bizcards.webservices.database.dao.UserDao;
import com.bizcards.webservices.database.model.Card;
import com.bizcards.webservices.database.model.User;
import com.bizcards.webservices.gcm.PNManager;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.bizcards.webservices.utils.Constants;


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

		List<CardBean> cardBeans = new ArrayList<CardBean>();
		cardBeans = CardDao.getInstance().getCardBeansWithUserId(userId);
		
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

			bean.userId = userId;
			
			card = CardDao.getInstance().add(bean);

			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true, "Succesfully Added Card", Status.OK.getStatusCode(), BeanConverter.getInstance().getCardBean(card)));
		} else {
			//update record
			card = BeanConverter.getInstance().getCard(bean);

			card = CardDao.getInstance().update(card.id, card);

			CardBean cardBean = BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(card.id));
	
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true, "Succesfully Updated Card", Status.OK.getStatusCode(), cardBean));
		}
	}
	
	@POST
	@Path("/share")
	public String addCardToUser(@Context HttpServletRequest hh, @QueryParam("receiver_id") String receiverId, @QueryParam("card_id") String cardId ){
				
		String senderId = hh.getAttribute(Constants.USER_ID).toString();

		if(CardDao.getInstance().getRecord(cardId) == null)
			return getErrorResponse(String.format("No Card found with Id %s",receiverId), Status.NO_CONTENT.getStatusCode());
		
		if(UserDao.getInstance().getRecordWithId(receiverId) == null)
			return getErrorResponse(String.format("No User found with Id %s",receiverId), Status.NO_CONTENT.getStatusCode());
		
		CardShareDao.getInstance().shareCardToUser(receiverId, senderId, cardId);
		
		boolean result = PNManager.getInstance().notifyCardShare(senderId, receiverId, cardId);
		
		if(!result)
			return getUnSuccesfullPushNotificationResponse();
		
		return getSuccessfulResponse();
	}
	
	@POST
	@Path("/share-with-name")
	public String addCardToUserWithUsername(@Context HttpServletRequest hh, @QueryParam("username") String username, @QueryParam("card_id") String cardId ){
				
		String senderId = hh.getAttribute(Constants.USER_ID).toString();

		if(CardDao.getInstance().getRecord(cardId) == null)
			return getErrorResponse(String.format("No Card found with Id %s", cardId), Status.NO_CONTENT.getStatusCode());
		
		User receiver = UserDao.getInstance().getRecordWithUsername(username);

		if(receiver == null)
			return getErrorResponse(String.format("No User found with Id %s", username), Status.NO_CONTENT.getStatusCode());
		
		CardShareDao.getInstance().shareCardToUser(receiver.id, senderId, cardId);
		
		boolean result = PNManager.getInstance().notifyCardShare(senderId, receiver.id, cardId);
		
		if(!result)
			return getUnSuccesfullPushNotificationResponse();
		
		return getSuccessfulResponse();
	}
	
	@GET
	@Path("/delete/{id}")
	public String delete(@PathParam("id") String id) {
		
		Card deleted = CardDao.getInstance().delete(id);

		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true,"Succesfully deleted Card",Status.OK.getStatusCode(),
				BeanConverter.getInstance().getCardBean(deleted)));

	}
	
	@GET
	@Path("/delete-permenant/{id}")
	public String deletePermanent(@PathParam("id") String id) {
		
		Card deleted = CardDao.getInstance().deletePermanently(id);
		
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<CardBean>(true,"Succesfully deleted Card",Status.OK.getStatusCode(),
				BeanConverter.getInstance().getCardBean(deleted)));
	}
}