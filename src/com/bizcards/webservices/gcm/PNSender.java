package com.bizcards.webservices.gcm;

import com.bizcards.webservices.gcm.model.PNContainer;
import com.bizcards.webservices.http.HttpResult;
import com.bizcards.webservices.http.SSHttpHelper;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.bizcards.webservices.utils.GDLogger;

public class PNSender {
	private static PNSender instance;
	
	private final String gcmUrl = "https://gcm-http.googleapis.com/gcm/send";

	public static PNSender getInstance() {
		if (instance == null){
			instance = new PNSender();
		}
		return instance; 
	}
	
	public boolean send(PNContainer container) {
		GDLogger.logInfo("PNSender", CommonJsonBuilder.getJsonForEntity(container));
		HttpResult result = new SSHttpHelper().postStringUrlFetch(gcmUrl, CommonJsonBuilder.getJsonForEntity(container));
		GDLogger.logInfo("PNSender", String.valueOf(result.statusCode));
		return result.statusCode == 200 ? true : false;
	}
	
}
