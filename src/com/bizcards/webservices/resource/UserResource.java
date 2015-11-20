package com.bizcards.webservices.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.ServerResponse;
import com.bizcards.webservices.database.bean.ChangePasswordBean;
import com.bizcards.webservices.database.bean.LoginResponse;
import com.bizcards.webservices.database.bean.UserBean;
import com.bizcards.webservices.database.dao.SessionDao;
import com.bizcards.webservices.database.dao.UserDao;
import com.bizcards.webservices.database.model.User;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.bizcards.webservices.utils.Constants;
import com.bizcards.webservices.utils.UniqueIdGenerator;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource extends BaseResource{

	@POST
	@Path("/sign-up")
	public String createNewUser(String data) {
		
		UserBean bean = CommonJsonBuilder.getEntityForJson(data, UserBean.class);
		if(bean == null)
			return getErrorResponse("Incorrect data", Status.NOT_MODIFIED.getStatusCode());

		if (UserDao.getInstance().getBean(bean.username) != null)
			return getErrorResponse("Username already exists. Please select another username", Status.CONFLICT.getStatusCode());

		bean.bizCardCode = UniqueIdGenerator.getInstance().getBizCardCode(bean.username);

        User user = UserDao.getInstance().add(bean);
        
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<User>(true,"Succesfully created user",200, user));
	}
	
	@GET
	@Path("/get")
	public String getUserBean(@QueryParam("username") String username,
			@QueryParam("password") String password) {
		UserBean userBean = UserDao.getInstance().getBean(username);
		userBean.password = null;
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<UserBean>(true,"Succesfully fetched details",200, userBean));
	}
	
	@POST
	@Path("/change-password")
	public String changePassword(String message) {
		ChangePasswordBean ub = CommonJsonBuilder.getEntityForJson(message, ChangePasswordBean.class);
		if(ub == null) 
			return getErrorResponse("Username and Passwords are not provided", Status.NOT_ACCEPTABLE.getStatusCode());
		
		if (!UserDao.getInstance().changePassword(ub))
			return getErrorResponse("Old Password does not match", Status.NOT_MODIFIED.getStatusCode());
		else
			return getSuccessfulResponse("Succesfully changed password", Status.OK.getStatusCode());
	}
	
	@POST
	@Path("/login")
	public String getAuthToken(String message) {
		UserBean ub = CommonJsonBuilder.getEntityForJson(message, UserBean.class);
		if(ub == null) 
			return getErrorResponse("Provide Username and Password", Status.NOT_ACCEPTABLE.getStatusCode());
		
		if(ub.devicePushNotificationId.isEmpty())
			return getErrorResponse("Provide devicePushNotificationIds", Status.NOT_ACCEPTABLE.getStatusCode());

		if (!UserDao.getInstance().isAuthorized(ub))
			return getUnAuthorizedServerResponse();
		
		String accessToken = SessionDao.getInstance().createSession(ub.username, ub.devicePushNotificationId);
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.accessToken = accessToken;
		loginResponse.userBean = BeanConverter.getInstance().getUserBean(UserDao.getInstance().getRecordWithUsername(ub.username));
;
		
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<LoginResponse>(true,"Login Succesful", Status.OK.getStatusCode(), loginResponse));
	}
	
	@POST
	@Path("/add-edit")
	public String createEditNewUser(String data) {
		
		UserBean bean = CommonJsonBuilder.getEntityForJson(data, UserBean.class);
		User user;
		if(bean.id == null){
			//create new record
			
			if (UserDao.getInstance().getBean(bean.username) != null)
				return getErrorResponse("Username already exists. Please select another username", Status.CONFLICT.getStatusCode());

			bean.bizCardCode = UniqueIdGenerator.getInstance().getBizCardCode(bean.name);
			user = UserDao.getInstance().add(bean);

			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<UserBean>(true, "Succesfully Added User", Status.OK.getStatusCode(), BeanConverter.getInstance().getUserBean(user)));
		} else {
			//update record
			user = BeanConverter.getInstance().getUser(bean);
			
			user = UserDao.getInstance().update(user);
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<UserBean>(true, "Succesfully Updated User", Status.OK.getStatusCode(), BeanConverter.getInstance().getUserBean(user)));
		}
	}
	
	@GET
	@Path("/get-all")
	public String getAllUsers(@QueryParam("restaurant_id") String restaurantId) {
		List<UserBean> response = UserDao.getInstance().getAll();
		
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<List<UserBean>>(response));
	}
	
	@GET
	@Path("/delete/{id}")
	public String delete(@PathParam("id") String id) {
		User deleted = UserDao.getInstance().delete(id);
		
		//Unmap all card of this particular user
		
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<UserBean>(true,"Succesfully deleted User",Status.OK.getStatusCode(),
				BeanConverter.getInstance().getUserBean(deleted)));
	}
	
	@GET
	@Path("/logout")
	public String logout(@Context HttpServletRequest hh) {

		String senderId = hh.getAttribute(Constants.USER_ID).toString();
		
		SessionDao.getInstance().deleteSession(SessionDao.getInstance().getAccessToken(UserDao.getInstance().getRecordWithId(senderId).username));
		return getSuccessfulResponse();
	}
}