package com.bizcards.webservices.gcm;

import java.util.ArrayList;
import java.util.List;

import com.bizcards.webservices.database.bean.CardShareBean;
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
		data.senderUsername = UserDao.getInstance().getRecordWithId(senderId).username;
		data.type = PNType.CARD_SHARED;
		data.cardShareId = cardShareId;
		container.data = data;

		User receiver = UserDao.getInstance().getRecordWithId(receiverId);
		List<String> devicePushNotificationIds = new ArrayList<String>();
		
		if (receiver != null) {
			devicePushNotificationIds = SessionDao.getInstance().getDevicePushNotificationId(receiver.username);

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
			devicePushNotificationIds = SessionDao.getInstance().getDevicePushNotificationId(sender.username);
			
			if (devicePushNotificationIds != null) {
				container.registration_ids.addAll(devicePushNotificationIds);
			}else
				return false;
		}
		
		PNSender.getInstance().send(container);

		return true;
	}

}
