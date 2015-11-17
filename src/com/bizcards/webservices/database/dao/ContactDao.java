package com.bizcards.webservices.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.bizcards.webservices.database.BeanConverter;
import com.bizcards.webservices.database.UpdateConverter;
import com.bizcards.webservices.database.bean.ContactBean;
import com.bizcards.webservices.database.model.Contact;
import com.bizcards.webservices.utils.UniqueIdGenerator;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class ContactDao  extends BaseDao implements IDao{
	private static Objectify ofy;
	private static ContactDao instance;
	
	static {
		ObjectifyService.register(Contact.class);		
	}

	public List<ContactBean> getContactBeansWithUserId(String userId){
		List<ContactBean> contactBeans = new ArrayList<ContactBean>();
		
		Query<Contact> query = ofy.query(Contact.class)
				.filter("userId", userId)
				.filter("isDeleted", false);
		if(query == null) return null;
		for(Contact contact : query) {
			contactBeans.add(BeanConverter.getInstance().getContactBean(contact));
		}
		return contactBeans;
	}
	
	public List<ContactBean> getAllContacts(){
		List<ContactBean> contactBeans = new ArrayList<ContactBean>();
		
		Query<Contact> query = ofy.query(Contact.class)
				.filter("isDeleted", false);
		if(query == null) return null;
		for(Contact contact : query) {
			contactBeans.add(BeanConverter.getInstance().getContactBean(contact));
		}
		return contactBeans;
	}
	
	public Contact getRecord(String contactId){
		Contact contact = ofy.query(Contact.class).filter("id", contactId).get();
		return contact;
	}
	
	public Contact update(String contactId, Contact contact){
		Contact fetchedContact = ofy.query(Contact.class).filter("id", contactId).get();
		fetchedContact = UpdateConverter.getInstance().getUpdatedContact(fetchedContact, contact);
		ofy.put(fetchedContact);
		return fetchedContact;
	}
	
	public Contact add(ContactBean contactBean){
		
		Contact contact = BeanConverter.getInstance().getContact(contactBean);
		contact.id = UniqueIdGenerator.getInstance().getId();
	
		ofy.put(contact);
		
		return contact;
	}
	
	public static ContactDao getInstance() {
		ObjectifyOpts opts = new ObjectifyOpts().setSessionCache(true);
		ofy = ObjectifyService.begin(opts);
		if (instance == null) instance = new ContactDao();
			return instance; 
	}

	public Contact delete(String id) {
		Contact fetchedObject = ofy.query(Contact.class).filter("id", id).get();
		fetchedObject.isDeleted = true;
		ofy.put(fetchedObject);
		return fetchedObject;
	}
	
	public Contact deletePermanently(String id) {
		Contact fetchedObject = ofy.query(Contact.class).filter("id", id).get();
		ofy.delete(fetchedObject);
		return fetchedObject;
	}
}
