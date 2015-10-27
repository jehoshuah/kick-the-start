package com.bizcards.webservices.database;

import com.bizcards.webservices.json.JsonInterface;


public class DataResultDummy<T> implements JsonInterface<T> {
	public boolean successful;
    public int statusCode;
    public T data;
}
