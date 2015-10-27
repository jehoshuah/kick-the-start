package com.bizcards.webservices.gcm.smack;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;

public class PushNotificationHelper {
	public void main() {
	    final String userName = "760596981585" + "@gcm.googleapis.com";
	    //final String password = "AIzaSyAGc00l4quY8PU_JDqgxx-qcUT7fE1R6ac";
	    //final String password = "AIzaSyBnzKR4s_6Ai_jDXQ1pOeERUC8wsazSClo";
	    final String password = "AIzaSyAwKE-wVii-NKH9S4sxprbFC91Y_ohNAiA";
	    
	    SmackCcsClient ccsClient = new SmackCcsClient();

	    try {
	      ccsClient.connect(userName, password);
	    } catch (XMPPException e) {
	      e.printStackTrace();
	    }

	    // Send a sample hello downstream message to a device.
	    //String toRegId = "APA91bFm53xqoQMYhSb9T3kzM85-J4hVScMN20E9bQjAXe7SEcBztggbmyNBwdLd-weuBn9kFasTN_te3xGEQfQ7kKsSe-5tXS69tlUqugPY_qtPgPZ8wQEVxunpqnDIUt-fLoulu7cURX97lnOHe_wI_1irtf67Oouq7vFWfjH4AjbC6yYsa5c";
	    String toRegId = " f7lQfzitzT8:APA91bFE4pDzcOHzZKvOH8MgO_x8AGIbvFuUe3Eh-62zbbfLZuB3i2GnvpTJBMIpNVAJOlJ4Xh7I6wAH4X4nbuei5TDMY7TlNoPwTlbF6m85aqlxYPJZZ4BHr7zbqQP9JPsakjGkkKGR";
	   
	    String messageId = ccsClient.getRandomMessageId();
	    Map<String, String> payload = new HashMap<String, String>();
	    payload.put("Hello", "World");
	    payload.put("CCS", "Dummy Message");
	    payload.put("EmbeddedMessageId", messageId);
	    String collapseKey = "sample";
	    Long timeToLive = 10000L;
	    Boolean delayWhileIdle = true;
	    ccsClient.send(SmackCcsClient.createJsonMessage(toRegId, messageId, payload, collapseKey,
	        timeToLive, delayWhileIdle));
	  }
}
