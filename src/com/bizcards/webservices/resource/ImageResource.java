package com.bizcards.webservices.resource;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.bizcards.webservices.database.ServerResponse;
import com.bizcards.webservices.database.dao.CardDao;
import com.bizcards.webservices.database.dao.ContactDao;
import com.bizcards.webservices.database.dao.UserDao;
import com.bizcards.webservices.database.model.Card;
import com.bizcards.webservices.database.model.Contact;
import com.bizcards.webservices.database.model.User;
import com.bizcards.webservices.json.CommonJsonBuilder;
import com.bizcards.webservices.utils.FileUploadHelperGCS;
import com.bizcards.webservices.utils.StoragePathHelper;
import com.sun.jersey.multipart.FormDataParam;

@Path("/image")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource extends BaseResource{
	
	@POST
	@Path("/card-logo")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String cardLogoUpload(@QueryParam("card_id") String cardId,
			@FormDataParam("logo") InputStream imageLogoInputStream) {
		Card card = CardDao.getInstance().getRecord(cardId);
		boolean fileLogoUploaded;
		
		if (card == null)
			return getErrorResponse(String.format("Invalid userId"), Status.BAD_REQUEST.getStatusCode());

		if (imageLogoInputStream == null)
			return getErrorResponse(String.format("logo is not specified in form parameters"), Status.BAD_REQUEST.getStatusCode());
				
		String objectLogoPath = StoragePathHelper.getCardLogoPath(cardId);

		fileLogoUploaded = FileUploadHelperGCS.getInstance().uploadImageFile(imageLogoInputStream, objectLogoPath);

		if(fileLogoUploaded){			
			card.logoImage = objectLogoPath;
			CardDao.getInstance().update(cardId, card);
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<String>(true, "Image Succesfuly Uploaded", Status.OK.getStatusCode(),
					StoragePathHelper.getResourceUrlGCS(objectLogoPath)));
		} else {
			return getUnSuccesfullImageResponse();
		}	
	}	
	
	@POST
	@Path("/card-image")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String cardUpload(@QueryParam("card_id") String cardId,
			@FormDataParam("image") InputStream imageInputStream) {
		Card card = CardDao.getInstance().getRecord(cardId);
		boolean fileImageUploaded;
		
		if (card == null)
			return getErrorResponse(String.format("Invalid cardId"), Status.BAD_REQUEST.getStatusCode());
		if (imageInputStream == null)
			return getErrorResponse(String.format("image is not specified in form parameters"), Status.BAD_REQUEST.getStatusCode());
		
		String objectImagePath = StoragePathHelper.getCardImagePath(cardId);
		
		fileImageUploaded = FileUploadHelperGCS.getInstance().uploadImageFile(imageInputStream, objectImagePath);

		if(fileImageUploaded){			
			card.imageUrl = objectImagePath;
			CardDao.getInstance().update(cardId, card);
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<String>(true, "Image Succesfuly Uploaded", Status.OK.getStatusCode(),
					StoragePathHelper.getResourceUrlGCS(objectImagePath)));
		} else {
			return getUnSuccesfullImageResponse();
		}	
	}	

	
	@POST
	@Path("/contact-image")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String contactUpload(@QueryParam("contact_id") String contactId,
			@FormDataParam("image") InputStream imageInputStream) {
		Contact contact = ContactDao.getInstance().getRecord(contactId);
		boolean fileImageUploaded;
		
		if (contact == null)
			return getErrorResponse(String.format("Invalid userId"), Status.BAD_REQUEST.getStatusCode());
		if (imageInputStream == null)
			return getErrorResponse(String.format("image is not specified in form parameters"), Status.BAD_REQUEST.getStatusCode());
		
		String objectImagePath = StoragePathHelper.getContactImagePath(contactId);
		
		fileImageUploaded = FileUploadHelperGCS.getInstance().uploadImageFile(imageInputStream, objectImagePath);

		if(fileImageUploaded){			
			contact.imageUrl = objectImagePath;
			ContactDao.getInstance().update(contactId, contact);
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<String>(true, "Image Succesfuly Uploaded", Status.OK.getStatusCode(),
					StoragePathHelper.getResourceUrlGCS(objectImagePath)));
		} else {
			return getUnSuccesfullImageResponse();
		}	
	}	

	
	@POST
	@Path("/user")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String userUpload(@QueryParam("user_id") String userId,
			@FormDataParam("image") InputStream imageInputStream,
			//@FormDataParam("image_crop") String imageCropParams,
			@FormDataParam("type") String type) {
		User user= UserDao.getInstance().getRecordWithId(userId);
		boolean fileUploaded;
		
		if (user == null)
			return getErrorResponse(String.format("Invalid userId"), Status.BAD_REQUEST.getStatusCode());
		if (imageInputStream == null)
			return getErrorResponse(String.format("image is not specified in form parameters"), Status.BAD_REQUEST.getStatusCode());
		
		//ImageCropParams imageCrop = CommonJsonBuilder.getEntityForJson(imageCropParams, ImageCropParams.class);
		String objectPath = StoragePathHelper.getUserImagePath(userId, type);
		
		//if ( imageCrop != null)
		//	fileUploaded = FileUploadHelperGCS.getInstance().uploadFileWithCrop(imageInputStream, imageCrop, objectPath);
		//else
			fileUploaded = FileUploadHelperGCS.getInstance().uploadImageFile(imageInputStream, objectPath);

		if(fileUploaded){			
			user.imageUrl = objectPath;
			UserDao.getInstance().update(user);
			return CommonJsonBuilder.getJsonForEntity(new ServerResponse<String>(true, "Image Succesfuly Uploaded", Status.OK.getStatusCode(),
					StoragePathHelper.getResourceUrlGCS(objectPath)));
		} else {
			return getUnSuccesfullImageResponse();
		}
	}
}