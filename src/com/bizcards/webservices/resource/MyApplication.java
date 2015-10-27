package com.bizcards.webservices.resource;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class MyApplication extends Application {
     public Set<Class<?>> getClasses() {
         Set<Class<?>> s = new HashSet<Class<?>>();
         s.add(TestResource.class);
         s.add(CardResource.class);
         s.add(UserResource.class);
         s.add(ImageResource.class);
         return s;
     }
}