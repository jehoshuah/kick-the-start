package com.bizcards.webservices.json;

public interface JsonBuilder {

	public <T> T getEntityForJson(String json, Class<T> entity);

	public String getJsonForEntity(JsonInterface entity);
}
