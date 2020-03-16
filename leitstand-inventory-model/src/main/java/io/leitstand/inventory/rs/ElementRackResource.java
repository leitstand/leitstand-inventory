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
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT_SETTINGS;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRack;
import io.leitstand.inventory.service.ElementRackLocation;
import io.leitstand.inventory.service.ElementRackService;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementRackResource {


	@Inject
	private ElementRackService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/rack")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
	public ElementRack getElementRack(@Valid @PathParam("id") ElementId id){
		return service.getElementRack(id);
	}
	
	@GET
	@Path("/{name}/rack")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
	public ElementRack getElementRack(@Valid @PathParam("name") ElementName name){
		return service.getElementRack(name);
	}
	
	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/rack/")
	public Response setElementRackMountPoint(@Valid @PathParam("element") ElementId id,
											 ElementRackLocation mountPoint){
		service.storeElementRackMountPoint(id, 
										   mountPoint);
		return success(messages);
	}
	
	@PUT
	@Path("/{element}/rack")
	public Response setElementRackMountPoint(@Valid @PathParam("element") ElementName name,
									  		 ElementRackLocation mountPoint){
		service.storeElementRackLocation(name, 
										 mountPoint);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element}/rack")
	public Response removeElementRackMountPoint(@Valid @PathParam("element") ElementName elementName){
		service.removeElementRackLocation(elementName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}/rack")
	public Response removeElementRackMountPoint(@Valid @PathParam("element") ElementId elementId){
		service.removeElementRackLocation(elementId);
		return success(messages);
	}
		
}
