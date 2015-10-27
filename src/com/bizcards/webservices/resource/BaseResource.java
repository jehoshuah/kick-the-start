package com.bizcards.webservices.resource;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import com.bizcards.webservices.database.ServerResponse;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.bizcards.webservices.utils.AuthHelper;

public class BaseResource {

	public boolean authenticate(@Context HttpHeaders hh) {
		return new AuthHelper().authenticate(hh);
	}
	
	public String getErrorResponse(String message, int statusCode){
		ServerResponse<Object> error = new ServerResponse<Object>(false, message, statusCode, null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	public String getSuccessfulResponse(String message, int statusCode){
		ServerResponse<Object> error = new ServerResponse<Object>(true, message, statusCode, null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	public String getSuccessfulResponse(){
		return getSuccessfulResponse("successful", 200);
	}
	
	public String getUnAuthorizedServerResponse(){
		ServerResponse<Object> error = new ServerResponse<Object>(false, "Un Authorized", Status.UNAUTHORIZED.getStatusCode(), null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	public String getNoResultsServerResponse(){
		ServerResponse<Object> error = new ServerResponse<Object>(false, "No results found", 204, null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	public String getSuccesfullyAddedServerResponse(){
		ServerResponse<Object> error = new ServerResponse<Object>(true, "Record added succesfully", 201, null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	public String getUnSuccesfullAddServerResponse(){
		ServerResponse<Object> error = new ServerResponse<Object>(false, "Unable to add record", Status.NOT_MODIFIED.getStatusCode(), null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	public String getUnSuccesfullResponse(){
		ServerResponse<Object> error = new ServerResponse<Object>(false, "Unable to process request", Status.NOT_MODIFIED.getStatusCode(), null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	public String getUnSuccesfullImageResponse(){
		ServerResponse<Object> error = new ServerResponse<Object>(false, "Unable to process Image request. Make sure crop parameters are within bounds and upload a proper image",
				Status.NOT_MODIFIED.getStatusCode(), null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	public String getUnSuccesfullPushNotificationResponse(){
		ServerResponse<Object> error = new ServerResponse<Object>(false, "Unable to push to Device. Try again",
				Status.NOT_MODIFIED.getStatusCode(), null);
		return CommonJsonBuilder.getJsonForEntity(error);
	}
	
	protected String getHeaderWithId(HttpHeaders hh, String headerTag){
		List<String> header = hh.getRequestHeader(headerTag);
		String result = null;
		if(header != null && header.get(0) != null){
			result = header.get(0);
			return result;
		}
		return result;
	}
}
