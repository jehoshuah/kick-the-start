package com.bizcards.webservices.database.bean;

import com.bizcards.webservices.json.JsonInterface;

public class CardShareBean implements JsonInterface<Object> {

	public String id;
    public String receiverId;
	public String senderId;
	public String sentTime;
	public String acceptedTime;
	public String cardId;
	
	public boolean isActive;
}
