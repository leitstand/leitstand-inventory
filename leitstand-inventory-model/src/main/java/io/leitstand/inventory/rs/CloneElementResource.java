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

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.security.auth.Role.OPERATOR;
import static io.leitstand.security.auth.Role.SYSTEM;
import static java.lang.String.format;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.CloneElementService;
import io.leitstand.inventory.service.ElementCloneRequest;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;

@RequestScoped
@Path("/elements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CloneElementResource {
	
	
	@Inject
	private CloneElementService service;
	
	@Inject
	private Messages messages;
	
	@POST
	@RolesAllowed({OPERATOR,SYSTEM})
	@Path("/{element:"+UUID_PATTERN+"}/_clone")
	public Response cloneElement(@PathParam("element") ElementId elementId,
								 ElementCloneRequest request) {

		ElementId cloneId = service.cloneElement(elementId, 
												 request);
		
		
		return created(format("/elements/%s/settings", 
							  cloneId),
					   messages);
	}
	
	@POST
	@RolesAllowed({OPERATOR,SYSTEM})
	@Path("/{element}/_clone")
	public Response cloneElement(@PathParam("element") ElementName elementName,
								 ElementCloneRequest request) {
		
		ElementId cloneId = service.cloneElement(elementName, 
				 								 request);

		return created(format("/elements/%s/settings", 
							  cloneId),
					   messages);
		
	}
	
}
