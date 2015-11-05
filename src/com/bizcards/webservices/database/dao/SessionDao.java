package com.bizcards.webservices.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.bizcards.webservices.database.model.Session;
import com.bizcards.webservices.utils.UniqueIdGenerator;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class SessionDao extends BaseDao implements IDao {
	
	private static Objectify ofy;
	private static SessionDao instance;
	private static int sessionTimeOut = 720; //in minutes. 12 hours
	
	static {
		ObjectifyService.register(Session.class);
	}
		
	public static SessionDao getInstance() {
		ObjectifyOpts opts = new ObjectifyOpts().setSessionCache(true);
		ofy = ObjectifyService.begin(opts);
		if (instance == null){
			instance = new SessionDao();
		}
		return instance; 
	}

	public String getUsername(String accessToken){
		
//		String currentTimeString = UniqueIdGenerator.getInstance().getCurrentTime();
		Session session = ofy.query(Session.class)
				.filter("accessToken", accessToken)
//				.filter("validTill >=", currentTimeString )
				.get();
		return session != null ? session.username : null;
	}
	
	public String getAccessToken(String username){
		
//		String currentTimeString = UniqueIdGenerator.getInstance().getCurrentTime();
		Session session = ofy.query(Session.class)
				.filter("username", username)
//				.filter("validTill >=", currentTimeString )
				.get();
		return session != null ? session.accessToken : null;
	}
	
	public String getUserId(String accessToken) {
//		String currentTimeString = UniqueIdGenerator.getInstance().getCurrentTime();

        Session session = ofy.query(Session.class).filter("accessToken", accessToken).get();
		return session != null ? UserDao.getInstance().getBean(session.username).id : null;
	}
 
	public List<String> getDevicePushNotificationId(String username) {
//		String currentTimeString = UniqueIdGenerator.getInstance().getCurrentTime();

		List<String> devicePushNotificationIds = new ArrayList<String>();
        Query<Session> sessions = ofy.query(Session.class).filter("username", username);
        
        for (Session session : sessions) {
        	devicePushNotificationIds.add(session.devicePushNotificationId);
        }
        
		return devicePushNotificationIds;
	}
	
	public String createSession(String username, String devicePushNotificationId) {
		
		Session session = new Session();
		session.accessToken = UniqueIdGenerator.getInstance().getAuthToken();
		session.username = username;
		session.devicePushNotificationId = devicePushNotificationId;
		session.validTill = UniqueIdGenerator.getInstance().getTimeAfter(sessionTimeOut);
		
		ofy.put(session);
		
		return session.accessToken;
	}
	
	public boolean deleteSession(String accessToken) {
		Session session = ofy.query(Session.class)
				.filter("accessToken", accessToken)
				.get();
		
		ofy.delete(session);
		return true;
	}

}