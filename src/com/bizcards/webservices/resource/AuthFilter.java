package com.bizcards.webservices.resource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import com.bizcards.webservices.database.ServerResponse;
import com.bizcards.webservices.database.dao.SessionDao;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.bizcards.webservices.utils.Constants;
import com.bizcards.webservices.utils.GDLogger;
import com.googlecode.objectify.ObjectifyService;

public class AuthFilter implements Filter {
	private FilterConfig filterConfig;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE, PUT");
		httpResponse.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, access_token");

		String accessToken = getAccessToken(httpRequest);
		//String userId = getUserId(getAccessToken(httpRequest));
		//request.setAttribute(AppConstants.USER_ID_KEY, userId);
        ObjectifyService.begin();

        if(httpRequest.getRequestURI().startsWith("/rest/api-docs")){
            GDLogger.logWarning("AuthFilter", "API docs URL so performing request");
            filterChain.doFilter(request, response);
            return;
        }else if (isExemptedUrl(httpRequest)) {
            GDLogger.logWarning("AuthFilter", "Exempted URL so performing request");
            filterChain.doFilter(request, response);
            return;
        } else if (accessToken == null) {
            GDLogger.logWarning("AuthFilter", "Access token not specified in header");
            getAccessDeniedResponse(httpResponse, "Access token not specified in header", Status.UNAUTHORIZED.getStatusCode(),
                    response.getWriter());
            return;
        } else if (!validateAccessToken(accessToken)) {
            GDLogger.logWarning("AuthFilter", "Not a valid access token");
            getAccessDeniedResponse(httpResponse, "Not a valid access token", Status.UNAUTHORIZED.getStatusCode(),
                    response.getWriter());
            return;
        }
        
        String userId = getUserId(getAccessToken(httpRequest));
		request.setAttribute(Constants.USER_ID, userId);
		
		filterChain.doFilter(request, response);

		return;
	}
	
	private void getAccessDeniedResponse(HttpServletResponse httpResponse, String message, int statusCode, PrintWriter printWriter) {
		printWriter.println(CommonJsonBuilder.getJsonForEntity(new ServerResponse<>(false, message, statusCode,
                null)));
		printWriter.close();
		
	}
    
	private boolean isExemptedUrl(HttpServletRequest requestCasted) {

		List<String> postExemptedList = new ArrayList<>();
		List<String> getExemptedList = new ArrayList<>();

		switch(requestCasted.getMethod()) {
			
			case "GET" :
				getExemptedList.add("/webservices/test/create-dummy");
				getExemptedList.add("/webservices/test/dummy");
				getExemptedList.add("/_ah/admin/datastore");

                for (String string : getExemptedList) {
                    if (requestCasted.getRequestURI().contains(string))
                        return true;
                }
			    break;
                
			case "POST" :
				postExemptedList.add("/webservices/user/login");

                postExemptedList.add("/webservices/user/sign-up");

                for (String string : postExemptedList) {
                    if (requestCasted.getRequestURI().contains(string))
                        return true;
                }
                break;
		}
		return false;
	}


	private String getAccessToken(HttpServletRequest request) {
		return request.getHeader("access_token");
	}

	private boolean validateAccessToken(String accessToken) {
		return getUserId(accessToken) != null;
	}

	private String getUserId(String accessToken) {
		return (SessionDao.getInstance().getUserId(accessToken));
	}

	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {

	}
  
}