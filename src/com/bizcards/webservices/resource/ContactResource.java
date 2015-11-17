package com.bizcards.webservices.resource;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.ServerResponse;
import com.bizcards.webservices.database.bean.ContactBean;
import com.bizcards.webservices.database.dao.ContactDao;
import com.bizcards.webservices.database.dao.UserDao;
import com.bizcards.webservices.database.model.Contact;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.bizcards.webservices.utils.Constants;


@Path("/contact")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContactResource extends BaseResource{
	
	@GET
	@Path("/get")
	public String getContact(@QueryParam("contact_id") String contactId) {
		ContactBean contactBean = BeanConverter.getInstance().getContactBean(ContactDao.getInstance().getRecord(contactId));
		
		if(contactBean == null)
			return getNoResultsServerResponse();
		else 
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<ContactBean>(true, "Fetched Contact Bean", Status.OK.getStatusCode(), contactBean));
	}
	
	@GET
	@Path("/get-all")
	public String getAllContacts() {
		List<ContactBean> contactBeans = new ArrayList<ContactBean>();
		contactBeans = ContactDao.getInstance().getAllContacts();
		
		if(contactBeans == null || contactBeans.isEmpty())
			return getNoResultsServerResponse();
		else 
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<List<ContactBean>>(true, "Fetched Contact Beans", Status.OK.getStatusCode(), contactBeans));
	}
	
	@GET
	@Path("/get-user-contacts")
	public String getUserContacts(@Context HttpServletRequest hh) {
		String userId = hh.getAttribute(Constants.USER_ID).toString();

		List<ContactBean> contactBeans = new ArrayList<ContactBean>();
		contactBeans = ContactDao.getInstance().getContactBeansWithUserId(userId);
		
		if(contactBeans == null || contactBeans.isEmpty())
			return getNoResultsServerResponse();
		else 
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<List<ContactBean>>(true, "Fetched Contact Beans of the User", Status.OK.getStatusCode(), contactBeans));
	}
	
	@POST
	@Path("/add")
	public String addContact(@Context HttpServletRequest hh, @QueryParam("card_id") String cardId){
		String userId = hh.getAttribute(Constants.USER_ID).toString();
		ContactBean contactBean = new ContactBean();
		contactBean.bizCardCode = UserDao.getInstance().getBizCardCodeWithId(userId);
		contactBean.cardId = cardId;
		Contact contact = ContactDao.getInstance().add(contactBean);

		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<ContactBean>(true, "Succesfully Added Contact", Status.OK.getStatusCode(), BeanConverter.getInstance().getContactBean(contact)));
	}
	
	@POST
	@Path("/add-edit")
	public String addEditContact(@Context HttpServletRequest hh, String data){
		String userId = hh.getAttribute(Constants.USER_ID).toString();

		ContactBean bean = CommonJsonBuilder.getEntityForJson(data, ContactBean.class);
		
		Contact contact;
		if(bean.id == null){

			bean.bizCardCode = UserDao.getInstance().getBizCardCodeWithId(userId);
			
			contact = ContactDao.getInstance().add(bean);

			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<ContactBean>(true, "Succesfully Added Contact", Status.OK.getStatusCode(), BeanConverter.getInstance().getContactBean(contact)));
		} else {
			//update record
			contact = BeanConverter.getInstance().getContact(bean);

			contact = ContactDao.getInstance().update(contact.id, contact);

			ContactBean contactBean = BeanConverter.getInstance().getContactBean(ContactDao.getInstance().getRecord(contact.id));
	
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<ContactBean>(true, "Succesfully Updated Contact", Status.OK.getStatusCode(), contactBean));
		}	
	}
	
	@GET
	@Path("/delete/{id}")
	public String delete(@PathParam("id") String id) {
		
		Contact deleted = ContactDao.getInstance().delete(id);

		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<ContactBean>(true,"Succesfully deleted Contact",Status.OK.getStatusCode(),
				BeanConverter.getInstance().getContactBean(deleted)));

	}
	
	@GET
	@Path("/delete-permenant/{id}")
	public String deletePermanent(@PathParam("id") String id) {
		
		Contact deleted = ContactDao.getInstance().deletePermanently(id);
		
		return CommonJsonBuilder.getJsonForEntity(new ServerResponse<ContactBean>(true,"Succesfully deleted Contact",Status.OK.getStatusCode(),
				BeanConverter.getInstance().getContactBean(deleted)));
	}

}
