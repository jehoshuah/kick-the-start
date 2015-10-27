package com.bizcards.webservices.resource;

import javax.ws.rs.core.HttpHeaders;

public interface IWebResource {
	abstract boolean authenticate(HttpHeaders hh);
}
