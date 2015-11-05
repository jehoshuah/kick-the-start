package com.bizcards.webservices.http;



import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.bizcards.webservices.utils.GDLogger;


public class SSHttpHelper {
	public HttpResult postStringUrlFetch(String urlParam, String data){

		HttpResult result = new HttpResult();
        try {
            URL url = new URL(urlParam);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "key=AIzaSyCOKz-cCG8fRLGKHKrqAsI2pIUD5zGWCAs");
            
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            GDLogger.logInfo("SSHttpHelper", data);
            writer.write(data);
            writer.close();
    
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                GDLogger.logInfo("SSHttpHelper success", data);
            } else {
            	GDLogger.logInfo("SSHttpHelper failure", data);
            }
            result.result = connection.getResponseMessage();
            result.statusCode = connection.getResponseCode();
        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
        return result;
	}
	
	public HttpResult postString(String url, String data) {
		HttpPost postRequest = new HttpPost();
		postRequest.addHeader("Content-Type", "application/json");
		//TODO: key hard coded
		postRequest.addHeader("Authorization", "key=AIzaSyDjUXQb1zNi84glae6PBLHpMMI0RBC6YVI");
		
		GDLogger.logInfo("SSHttpHelper", "Data sent to server" + data);
		
		try {
			if(data != null) postRequest.setEntity(new StringEntity(data.trim(), HTTP.UTF_8));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return doRequest(postRequest, url);
	}


	public HttpResult postNameValuePairs(String url, Map<String, String> data) {
		List<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();

		for (Entry<String, String> entry : data.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		HttpPost postRequest = new HttpPost();
		try {
			if (data != null) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs);
				postRequest.setEntity(entity);
			}
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return doRequest(postRequest, url);
	}
	
	private HttpResult doRequest(HttpRequestBase request, String url) {
		HttpResult httpResult = null;
		DefaultHttpClient client =  new DefaultHttpClient();
		try {
			request.setURI(URI.create(url.trim()));
			//setupRequest(request);
			httpResult = getHttpResult(client.execute(request));
			client.getConnectionManager().shutdown();
		}
		catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
		}
		catch (IOException e) {
			client.getConnectionManager().shutdown();
		}
		catch (Exception e) {
			client.getConnectionManager().shutdown();
		}
		return httpResult;
	}

	private HttpResult getHttpResult(HttpResponse httpResponse) throws ParseException, IOException {
		String responseMessage = null;
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) responseMessage = EntityUtils.toString(httpEntity);
		HttpResult httpResult = new HttpResult();
		httpResult.result = responseMessage;
		int statusCode = httpResponse.getStatusLine() == null ? 0 : httpResponse.getStatusLine().getStatusCode();
		httpResult.statusCode = statusCode;
		return httpResult;
	}
}
