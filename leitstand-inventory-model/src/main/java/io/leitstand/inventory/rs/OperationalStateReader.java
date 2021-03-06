/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
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
