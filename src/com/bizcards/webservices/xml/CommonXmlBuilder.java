package com.bizcards.webservices.xml;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.bizcards.webservices.utils.GDLogger;

public class CommonXmlBuilder {

	private static String LOG_TAG ="CommonXmlBuilder";
	
	public static <T> List<T> getEntityForXml(String xmlDoc, Class<T> entity) {
		List<T> results = new ArrayList<T>();
		Serializer xmlSerializer = new Persister();
		try {
			T result = xmlSerializer.read(entity, xmlDoc);
			if (result != null) results.add(result);
			return results;
		}
		catch (Exception e) {
			GDLogger.logInfo(LOG_TAG, String.format("%s  ", e.getStackTrace()));
		}

		return null;
	}

	public static <T> String getXmlForEntity(XmlInterface<T> entity) {
		Serializer xmlSerializer = new Persister();
		StringWriter stringWriter = new StringWriter();
		try {
			xmlSerializer.write(entity, stringWriter);
			return stringWriter.toString();
		}
		catch (Exception e) {
			GDLogger.logInfo(LOG_TAG, String.format("%s  ", e.getStackTrace()));
		}
		return null;
	}
}
