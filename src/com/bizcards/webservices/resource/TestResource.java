package com.bizcards.webservices.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.bizcards.webservices.database.dummydata.DummyData;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;


@Path("/test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestResource extends BaseResource{
	
	@GET
	@Path("/create-dummy")
	public String createDummyDataMinimal(@QueryParam("token") String token) {
		
		if(token == null || !token.equals("ting-ting"))
			return getErrorResponse(String.format("Invalid token"), Status.FORBIDDEN.getStatusCode());
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Queue queue = QueueFactory.getDefaultQueue();
		try {
		    com.google.appengine.api.datastore.Transaction txn = ds.beginTransaction();

		    queue.add(TaskOptions.Builder.withUrl("/webservices/test/dummy").method(TaskOptions.Method.GET));

		    txn.commit();
		} catch (DatastoreFailureException e) {
		}
		
		return "Done";
	}
	
	@GET
	@Path("/dummy")
	public String getDummy() {
		try {
			new DummyData().createDummyData();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CommonJsonBuilder.getJsonForEntity("created dummy data");
	}
	
}