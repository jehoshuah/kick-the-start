package com.bizcards.webservices.gcm.model;

import com.bizcards.webservices.database.bean.CardBean;


public class PNData {
	public int type;
	public String receiverId;
	public String senderId;
	public String senderUsername;
	public String receiverUsername;
	public String cardId;
	public CardBean card;
	
	public String cardShareId;
}
