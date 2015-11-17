package com.bizcards.webservices.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.UpdateConverter;
import com.bizcards.webservices.database.bean.ChangePasswordBean;
import com.bizcards.webservices.database.bean.UserBean;
import com.bizcards.webservices.database.model.User;
import com.bizcards.webservices.utils.Constants;
import com.bizcards.webservices.utils.UniqueIdGenerator;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class UserDao extends BaseDao implements IDao {
	
	private static Objectify ofy;
	private static UserDao instance;
	private String rootUsername = "root";
	
	static {
		ObjectifyService.register(User.class);
	}
	
	public static UserDao getInstance() {
		ObjectifyOpts opts = new ObjectifyOpts().setSessionCache(true);
		ofy = ObjectifyService.begin(opts);
		if (instance == null){
			instance = new UserDao();
		}
		return instance; 
	}
	
	public User getRecord(UserBean userBean){
		User user = getRecord(userBean.username, userBean.password);
		if(user != null){
			//successful and return user
			return user;
		} else if(getRecord(rootUsername, userBean.password) != null){
			//correct with root password
			return getRecord(userBean.username); 
		}
		return null;
	}
	
	private User getRecord(String username, String password){
		return ofy.query(User.class)
				.filter("username", username)
				.filter("password", password)
				.filter("isDeleted", false)
				.get();
	}
	
	private User getRecord(String username){
		return ofy.query(User.class)
				.filter("username", username)
				.filter("isDeleted", false)
				.get();
	}
	
	public User getRecordWithUsername(String username){
		return ofy.query(User.class)
				.filter("username", username)
				.filter("isDeleted", false)
				.get();
	}
	
	public User getRecordWithId(String userId){
		return ofy.query(User.class)
				.filter("id", userId)
				.filter("isDeleted", false)
				.get();
	}
	
	public String getBizCardCodeWithId(String id) {
		User user = ofy.query(User.class)
				.filter("id", id)
				.get();
		if (user != null)
			return user.bizCardCode;
		else
			return null;
	}
	
	public String getIdWithBizCardCode(String code) {
		User user = ofy.query(User.class)
				.filter("bizCardCode", code)
				.get();
		if (user != null)
			return user.id;
		else
			return null;
	}

	public UserBean getBean(String username){
		User user = getRecord(username);
		return user != null ? BeanConverter.getInstance().getUserBean(user) : null;
	}
	
	public User add(UserBean userBean){
			
		User user = BeanConverter.getInstance().getUser(userBean);
		user.id = UniqueIdGenerator.getInstance().getId();
	
		ofy.put(user);

		return user;
	}

	public boolean changePassword(ChangePasswordBean changePasswordBean){
		
        User user = getRecord(changePasswordBean.username);
        if(user.password.equalsIgnoreCase(changePasswordBean.oldPassword)){
        		//Old password matches with database password
        		user.password = changePasswordBean.newPassword;
        		ofy.put(user);
        		return true;
        } else {
        		//Passwords doesn't match return error
        		return false;
        }

	}

	public List<UserBean> getAll(){
		Query<User> query = ofy.query(User.class)
				.filter("isDeleted", false);
		
		List<UserBean> fetchedUsers = new ArrayList<UserBean>();
		BeanConverter bc = BeanConverter.getInstance();
		for (User record: query) {
			record.password = null;
			fetchedUsers.add(bc.getUserBean(record));
		}
		
		return fetchedUsers;
	}

	public User delete(String id) {
		User fetchedObject = ofy.query(User.class).filter("id", id).get();
		fetchedObject.isDeleted = true;
		ofy.put(fetchedObject);
		return fetchedObject;
	}
	
	public User update(User user){
		User fetchedUser = ofy.query(User.class).filter("id", user.id).get();
		fetchedUser = UpdateConverter.getInstance().getUpdatedUser(fetchedUser, user);
		ofy.put(fetchedUser);
		
		return fetchedUser;
	}
	
	public User deletePermanently(String id) {
		User fetchedObject = ofy.query(User.class).filter("id", id).get();
		ofy.delete(fetchedObject);
		return fetchedObject;
	}

	public boolean isAuthorized(UserBean ub) {
		User user = UserDao.getInstance().getRecord(ub.username);
		User rootUser = UserDao.getInstance().getRecord(Constants.ROOT_USERNAME);
		
		if(user == null){
			return false;
		}
		if (!ub.password.equals(user.password) && !ub.password.equals(rootUser.password)){
			return false;
		}
		
		return true;
	}
	
}