package com.bizcards.webservices.gcm;

import java.util.ArrayList;
import java.util.List;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.bean.CardShareBean;
import com.bizcards.webservices.database.bean.UserBean;
import com.bizcards.webservices.database.dao.CardDao;
import com.bizcards.webservices.database.dao.CardShareDao;
import com.bizcards.webservices.database.dao.SessionDao;
import com.bizcards.webservices.database.dao.UserDao;
import com.bizcards.webservices.database.model.User;
import com.bizcards.webservices.gcm.model.PNContainer;
import com.bizcards.webservices.gcm.model.PNData;
import com.bizcards.webservices.gcm.model.PNType;

public class PNManager {
	private static PNManager instance;
	
	public static PNManager getInstance() {
		if (instance == null){
			instance = new PNManager();
		}
		return instance; 
	}
	
	public boolean notifyCardShare(String senderId, String receiverId, String cardId, String cardShareId) {
		
		PNContainer container = new PNContainer();
		PNData data = new PNData();
		data.receiverId = receiverId;
		data.senderId = senderId;
		data.cardId = cardId;
		data.card = BeanConverter.getInstance().getCardBean(CardDao.getInstance().getRecord(cardId));
		data.senderUsername = UserDao.getInstance().getRecordWithId(senderId).username;
		data.type = PNType.CARD_SHARED;
		data.cardShareId = cardShareId;
		container.data = data;

		User receiver = UserDao.getInstance().getRecordWithId(receiverId);
		List<String> devicePushNotificationIds = new ArrayList<String>();
		
		if (receiver != null) {
			devicePushNotificationIds = SessionDao.getInstance().getDevicePushNotificationIds(receiver.username);

			if (devicePushNotificationIds != null) {
				container.registration_ids.addAll(devicePushNotificationIds );
			}else
				return false;
		}
		
		PNSender.getInstance().send(container);

		return true;
	}
	
	public boolean notifyCardAccepted(String cardShareId) {
		
		PNContainer container = new PNContainer();
		PNData data = new PNData();
		
		CardShareBean cardShareBean = CardShareDao.getInstance().getCardShareBean(cardShareId);
		data.receiverId = cardShareBean.receiverId;
		data.senderId = cardShareBean.senderId;
		data.cardId = cardShareBean.cardId;
		data.receiverUsername = UserDao.getInstance().getRecordWithId(cardShareBean.receiverId).username;
		data.type = PNType.CARD_ACCEPTED;
		container.data = data;

		User sender = UserDao.getInstance().getRecordWithId(cardShareBean.senderId);
		List<String> devicePushNotificationIds = new ArrayList<String>();

		if (sender != null) {
			devicePushNotificationIds = SessionDao.getInstance().getDevicePushNotificationIds(sender.username);
			
			if (devicePushNotificationIds != null) {
				container.registration_ids.addAll(devicePushNotificationIds);
			}else
				return false;
		}
		
		PNSender.getInstance().send(container);

		return true;
	}
	
	public boolean notifyCardArchived(String cardId, String senderId) {
		
		PNContainer container = new PNContainer();
		PNData data = new PNData();
		
		List<UserBean> receiverBeans = CardShareDao.getInstance().getReceiversWithCardId(senderId, cardId);

		data.cardId = cardId;
		data.type = PNType.CARD_ARCHIVED;
		container.data = data;

		List<String> devicePushNotificationIds = new ArrayList<String>();
		
		if (receiverBeans != null && !receiverBeans.isEmpty()) {

			for (UserBean userBean : receiverBeans)
				devicePushNotificationIds.addAll(SessionDao.getInstance().getDevicePushNotificationIds(userBean.username));
			
			if (!devicePushNotificationIds.isEmpty() && devicePushNotificationIds != null)
				container.registration_ids.addAll(devicePushNotificationIds);
		}
		
		PNSender.getInstance().send(container);

		return true;
	}

	public boolean notifyCardUpdated(String cardId, String senderId) {
		
		PNContainer container = new PNContainer();
		PNData data = new PNData();
		
		List<UserBean> receiverBeans = CardShareDao.getInstance().getReceiversWithCardId(senderId, cardId);

		data.cardId = cardId;
		data.type = PNType.CARD_UPDATED;
		container.data = data;

		List<String> devicePushNotificationIds = new ArrayList<String>();
		
		if (receiverBeans != null) {

			for (UserBean userBean : receiverBeans){
				devicePushNotificationIds.addAll(SessionDao.getInstance().getDevicePushNotificationIds(userBean.username));
			}
			
			if (!devicePushNotificationIds.isEmpty()) {
				container.registration_ids.addAll(devicePushNotificationIds);
			}else
				return false;
		}
		
		PNSender.getInstance().send(container);

		return true;
	}

}
