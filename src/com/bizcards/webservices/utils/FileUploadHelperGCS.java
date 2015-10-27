package com.bizcards.webservices.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.common.io.ByteStreams;

public class FileUploadHelperGCS {

	private final GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
	private final String imageType = "image/jpeg";
	private final String dumpType = "text/plain";
	private static FileUploadHelperGCS instance;
	
	public static FileUploadHelperGCS getInstance() {
		if (instance == null) instance = new FileUploadHelperGCS();
		return instance; 
	}
	
	public boolean uploadDumpFile(InputStream fileInputStream, String objectPath) {
		return uploadFile(fileInputStream, objectPath, dumpType);
	}
	
	public boolean uploadImageFile(InputStream fileInputStream, String objectPath) {
		return uploadFile(fileInputStream, objectPath, imageType);
	}
	
	private boolean uploadFile(InputStream fileInputStream, String objectPath, String mimeType) {
		if(fileInputStream == null){
			return false;
		}
		try {
			GcsFileOptions.Builder builder = new GcsFileOptions.Builder();
			builder.mimeType(mimeType);
			
			GcsFilename fileName = new GcsFilename(Constants.getBucketName(), objectPath);
			GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, builder.build());
			OutputStream ostream = Channels.newOutputStream(outputChannel);
			ByteStreams.copy(fileInputStream, ostream);
			ostream.close();
			fileInputStream.close();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		return false;
	}
	
	public boolean uploadFile(byte[] byteArray, String objectPath) {
		if(byteArray == null){
			return false;
		}
		try {
			GcsFileOptions.Builder builder = new GcsFileOptions.Builder();
			builder.mimeType(imageType);
			
			GcsFilename fileName = new GcsFilename(Constants.getBucketName(), objectPath);
			GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, builder.build());
			OutputStream ostream = Channels.newOutputStream(outputChannel);
			ostream.write(byteArray);
			ostream.close();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		return false;

	}

//	public boolean uploadFileWithCrop(InputStream inputStream, ImageCropParams cropParams, String objectPath){
//		
//		byte[] bytes;
//		try {
//			bytes = IOUtils.toByteArray(inputStream);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//		Image originalImage = ImagesServiceFactory.makeImage(bytes);
//		Image cropedImage;
//		if(!isCropParamsValid(cropParams, originalImage)){
//			return false;
//		}
//		if(cropParams != null){
//			int height = originalImage.getHeight();
//			int width = originalImage.getWidth();
//			
//			Transform transform = ImagesServiceFactory.makeCrop(cropParams.x1/width, cropParams.y1/height, cropParams.x2/width, cropParams.y2/height);
//			cropedImage = ImagesServiceFactory.getImagesService().applyTransform(transform, originalImage);
//		} else {
//			cropedImage = originalImage;
//		}
//		
//		if(FileUploadHelperGCS.getInstance().uploadFile(cropedImage.getImageData(), objectPath)){
//			return true;
//		}
//		
//		return false;
//	}
	
}
