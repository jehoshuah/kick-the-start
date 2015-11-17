package com.bizcards.webservices.utils;

import java.util.Date;

import com.bizcards.webservices.database.dao.UserDao;
import com.bizcards.webservices.database.model.User;

public class UniqueIdGenerator {

	private static UniqueIdGenerator instance;
	
	public static UniqueIdGenerator getInstance() {
		if(instance == null) instance = new UniqueIdGenerator();
		return instance;
	}
	
	@SuppressWarnings("static-access")
	public String getId(){
		try {
			Thread.currentThread().sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Long.toString(new Date().getTime());
	}
	
	public String getCurrentTime(){
		return Long.toString(new Date().getTime());
	}
	
	public String getTimeAfter(int minutes){
		return Long.toString(new Date().getTime() + minutes * 60000);
	}
	
	public String getAuthToken(){
		return EncryptDecrypt.getSha2Hash(getCurrentTime());
	}
	
	public String getBizCardCode(String name){
		String code = String.format("%s.%s", name.toLowerCase(), getHash());
		boolean duplicate = true;
		while(duplicate) {
			User user = UserDao.getInstance().getUserWithBizCardCode(code);
			if (user == null)
				duplicate = false;
		};
		return code;
	}
	
    public String getHash()
    {
        String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        String shuffledString = "";
        while (s.length() != 0)
        {
            int index = (int) Math.floor(Math.random() * s.length());
            char c = s.charAt(index);
            s = s.substring(0,index)+s.substring(index+1);
            shuffledString += c;
        }

        shuffledString = shuffledString.substring(0, Math.min(shuffledString.length(), 5));
        return shuffledString;
    }
	
}
