/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import io.leitstand.inventory.service.OperationalState;

@Provider
@Consumes(APPLICATION_JSON)
public class OperationalStateReader implements MessageBodyReader<OperationalState> {

	@Override
	public boolean isReadable(Class<?> type, 
							  Type genericType, 
							  Annotation[] annotations, 
							  MediaType mediaType) {
		return true;
	}

	@Override
	public OperationalState readFrom(Class<OperationalState> type, 
									 Type genericType, 
									 Annotation[] annotations,
									 MediaType mediaType, MultivaluedMap<String, String> httpHeaders, 
									 InputStream entityStream)
			throws IOException, WebApplicationException {
		try (JsonReader reader = Json.createReader(entityStream)){
			return OperationalState.valueOf(((JsonString)reader.readValue()).getString());
		} 
	}


}
