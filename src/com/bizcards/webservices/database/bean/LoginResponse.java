package com.bizcards.webservices.database.bean;

import com.bizcards.webservices.json.JsonInterface;

public class LoginResponse  implements JsonInterface<Object> {

	public UserBean userBean;
	public String accessToken;

}
