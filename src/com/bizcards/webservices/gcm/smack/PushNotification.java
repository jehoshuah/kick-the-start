package com.bizcards.webservices.gcm.smack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class PushNotification {
	private static PushNotification instance;
	
	public static PushNotification getInstance() {
		if (instance == null){
			instance = new PushNotification();
		}
		return instance; 
	}
	
	public void sendNotification(String deviceRegId) {
		try {
			sendPost();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void sendPost() throws Exception {
		 
		String url = "https://gcm-http.googleapis.com/cloud-messaging/send";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "key=AIzaSyAwKE-wVii-NKH9S4sxprbFC91Y_ohNAiA");
		
		//String urlParameters = "{\"registration_ids\" : [\"APA91bEq11iZkVk4AQ8xJ0Ll_h9NjP8zwKXc817RCk3Nlow5RQyRwZNbui94BMJhRjaElxIOUp2pYADpMV1Dz1dtp_VHZRTQEUdcF5pyHoXKtfcWule7vrmvbZncOl7-kKCY5OpJyWb1ZBMC7aoJq9ctF0m_AuWj_5RyQmTrdVrVPx7P_y-hkcU\"], \"data\" : { \"abc\":\"def\"  },	}";
		String urlParameters = "{\"registration_ids\" : [\"f7lQfzitzT8:APA91bFE4pDzcOHzZKvOH8MgO_x8AGIbvFuUe3Eh-62zbbfLZuB3i2GnvpTJBMIpNVAJOlJ4Xh7I6wAH4X4nbuei5TDMY7TlNoPwTlbF6m85aqlxYPJZZ4BHr7zbqQP9JPsakjGkkKGR\"], \"data\" : { \"abc\":\"def\"  },	}";
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
 
	}
	
	/*
	public boolean sendMessage() {
		
		final String GCM_API_KEY = "AIzaSyAwKE-wVii-NKH9S4sxprbFC91Y_ohNAiA";
	    final int retries = 3;
	    final String notificationToken = "deviceNotificationToken";
	    Sender sender = new Sender(GCM_API_KEY);
	    Message msg = new Message.Builder().build();

	    try {
	                Result result = sender.send(msg, notificationToken, retries);

	                if (StringUtils.isEmpty(result.getErrorCodeName())) {
	                    logger.debug("GCM Notification is sent successfully");
	                    return true;
	                }

	                logger.error("Error occurred while sending push notification :" + result.getErrorCodeName());
	    } catch (InvalidRequestException e) {
	                logger.error("Invalid Request", e);
	    } catch (IOException e) {
	                logger.error("IO Exception", e);
	    }
	    return false;
	}
	*/
}
