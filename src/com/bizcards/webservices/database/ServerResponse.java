package com.bizcards.webservices.database;

import com.bizcards.webservices.json.JsonInterface;

public class ServerResponse<T> implements JsonInterface<T>{
	public String message;
	public int statusCode;
	public boolean successful;
	public T data;
	public ServerResponse(boolean successful, String message, int statusCode, T data) {
		this.successful = successful;
		this.message = message;
		this.statusCode = statusCode;
		this.data = data;
	}
	public ServerResponse(T data){
		this.successful = true;
		this.message = "successful";
		this.statusCode = 200;
		this.data = data;
	}
	public ServerResponse() {
		
	}
}
