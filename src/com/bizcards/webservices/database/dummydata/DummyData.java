package com.bizcards.webservices.database.dummydata;

import java.util.HashMap;
import java.util.Map;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.bean.CardBean;
import com.bizcards.webservices.database.bean.UserBean;
import com.bizcards.webservices.database.dao.CardDao;
import com.bizcards.webservices.database.dao.UserDao;
import com.bizcards.webservices.database.model.User;
import com.bizcards.webservices.utils.Constants;
import com.bizcards.webservices.utils.EncryptDecrypt;

public class DummyData {

	
	private final String imageRootUrl = String.format("%s/data/images", Constants.getRootUrl());
	
	Map<String,String> usersMap = new HashMap<String,String>();
	
	private String getImageUrl(String url){
		return new StringBuilder().append(imageRootUrl).append(url).toString();
	}
	
	public boolean createDummyData() throws InterruptedException{
		
		createDummyUsers();
		createDummyCards();
		
		return true;
	}
	
	private UserBean createUserInDb(String username, String password, String name, String email,String phone){
		UserBean ub = new UserBean();

		ub.name = name;
		ub.username = username;
		ub.email = email;
		ub.phone  = phone;
		ub.password = EncryptDecrypt.getSha2Hash(password);
		ub.imageUrl = getImageUrl("/product/administrator-icon.png");
		
		ub = BeanConverter.getInstance().getUserBean(UserDao.getInstance().add(ub));
		
		return ub;
	}

	private void createDummyUsers() {
		
		createUserInDb(Constants.ROOT_USERNAME, "root", "Root User", "root@gmail.com", "root phone");
		usersMap.put("jd", createUserInDb("joshua", "joshua", "Dasari Joshua", "joshuad@gendevs.com", "jd phone").id);
		usersMap.put("db", createUserInDb("deepak", "deepak", "Deepak Bandela", "deepak@gendevs.com", "db phone").id);
	}
	
	private CardBean createCardInDb(String userMapId, String companyName, String designation, String fax, String phone, String website){
		CardBean cb = new CardBean();
		
		User user = UserDao.getInstance().getRecordWithId(usersMap.get(userMapId));
		
		cb.userFullName = user.name;
		cb.imageUrl = getImageUrl("/product/administrator-icon.png");
		cb.userId = user.id;
		cb.companyName = companyName;
		cb.designation = designation;
		cb.fax = fax;
		cb.phone = phone;
		cb.website = website;
		
		cb.logoImage = getImageUrl("/product/administrator-icon.png");
		
		cb = BeanConverter.getInstance().getCardBean(CardDao.getInstance().add(cb));
		
		return cb;
	}

	private void createDummyCards() {
		
		createCardInDb("jd", "Joshua Company", "Developer", "joshuad@gendevs.com", "jd phone", "www.jehoshuah.com");
		createCardInDb("db", "General Developers", "Administrator", "deepak@gendevs.com", "db phone", "www.deepakbandela.com");
	}
	
}
