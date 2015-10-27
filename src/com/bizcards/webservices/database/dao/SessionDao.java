package com.bizcards.webservices.database.dao;

import com.bizcards.webservices.database.model.Session;
import com.bizcards.webservices.utils.UniqueIdGenerator;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.ObjectifyService;

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
	
	public String getUserId(String accessToken) {
		String currentTimeString = UniqueIdGenerator.getInstance().getCurrentTime();

        Session session = ofy.query(Session.class).filter("accessToken", accessToken).filter("validTill >=", currentTimeString).get();
		return session != null ? UserDao.getInstance().getBean(session.username).id : null;
	}
 

	public String createSession(String username) {
		
		Session session = new Session();
		session.accessToken = UniqueIdGenerator.getInstance().getAuthToken();
		session.username = username;
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