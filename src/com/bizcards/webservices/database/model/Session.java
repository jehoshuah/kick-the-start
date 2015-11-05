package com.bizcards.webservices.database.model;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;

@Cached
public class Session
{
	@Indexed @Id
    public String accessToken;
    @Indexed
    public String username;
    @Indexed
    public String validTill;
    public String devicePushNotificationId;
}
