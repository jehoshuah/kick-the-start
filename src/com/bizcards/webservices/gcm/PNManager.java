package com.bizcards.webservices.gcm;

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
	
	public boolean notifyCardShare(String senderId, String receiverId, String cardId) {
		
		PNContainer container = new PNContainer();
		PNData data = new PNData();
		data.receiverId = receiverId;
		data.senderId = senderId;
		data.cardId = cardId;
		data.type = PNType.CARD_SHARED;
		container.data = data;

		User sender = UserDao.getInstance().getRecordWithId(senderId);
		if (sender != null) {
			if (sender.devicePushNotificationId != null) {
				container.registration_ids.add(sender.devicePushNotificationId );
			}
		}
		
		PNSender.getInstance().send(container);

		return true;
	}

}
