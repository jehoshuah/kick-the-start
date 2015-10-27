package com.bizcards.webservices.database.model;

import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;

@Cached
public class CardShare
{
    @Id
    @Indexed
    public String id;
    @Indexed
    public String receiverId;
    @Indexed
	public String senderId;
    public Date sentTime;
	public Date acceptedTime;
	public String cardId;
}
