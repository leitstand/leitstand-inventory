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
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.AccessDeniedException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementService;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.security.auth.ReasonCode;
import io.leitstand.security.auth.Scopes;
import io.leitstand.security.auth.UserContext;

@Resource
@Scopes({IVT, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementResource {
	
	private static final Logger LOG = Logger.getLogger(ElementResource.class.getName()); 

	public static final String FILTER_BY_ASSET_ID = "assetid";
	public static final String FILTER_BY_NAME = "name";
	public static final String FILTER_BY_NAME_AND_TAG = "tag";
	public static final String FILTER_BY_SERIAL_NUMBER = "serial";
	public static final String FILTER_BY_MANAGEMENT_IP = "ip";
	
	@Inject
	private ElementService service;
	
	@Inject
	private Messages messages;
	
	@Inject
	private UserContext user;
	
	@GET
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
	public List<ElementSettings> findElement(@QueryParam("filter") @DefaultValue("") String filter,
											 @QueryParam("by") @DefaultValue(FILTER_BY_NAME_AND_TAG) String by,
	                                         @QueryParam("offset") @DefaultValue("0") int offset,
											 @QueryParam("limit")  @DefaultValue("100") int limit){
		
		switch(by) {
			case FILTER_BY_NAME:
				return service.findElementsByName(filter, offset, limit);
			case FILTER_BY_NAME_AND_TAG:
				return service.findElementsByNameOrTag(filter, offset, limit);
			case FILTER_BY_MANAGEMENT_IP:
				return service.findElementsByManagementIP(filter, offset, limit);
			case FILTER_BY_SERIAL_NUMBER:
				return service.findElementsBySerialNumber(filter, offset, limit);
			case FILTER_BY_ASSET_ID:
				return service.findElementsByAssetId(filter, offset, limit);
			default:{
				LOG.fine(() -> String.format("Unknown filter criteria %s. Use default %s option instead.",by,FILTER_BY_NAME_AND_TAG));
				return service.findElementsByNameOrTag(filter, offset, limit);
			}
		}
	}
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}")
	public Response removeElement(@PathParam("element") ElementId elementId,
								  @QueryParam("force") boolean force) {
		if(force) {
			verifyForceAllowed();
			service.forceRemoveElement(elementId);
		} else {
			service.removeElement(elementId);
		}
		return success(messages);
	}

	@DELETE
	@Path("/{element}")
	public Response removeElement(@PathParam("element") ElementName elementName,
							  	  @QueryParam("force") boolean force) {
		if(force) {
			verifyForceAllowed();
			service.forceRemoveElement(elementName);
		} else {
			service.removeElement(elementName);
		}
		return success(messages);
	}
	
	private void verifyForceAllowed() {
		if(!user.scopesIncludeOneOf(IVT,IVT_ELEMENT)) {
			throw new AccessDeniedException(ReasonCode.AUT0002E_SCOPE_ACCESS_DENIED, format("%s %s",IVT,IVT_ELEMENT));
		}
	}
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/operational_state")
	public Response updateOperationalState(@Valid @PathParam("id") ElementId id, 
										   OperationalState state){
		service.updateElementOperationalState(id,state);
		return success(messages);
	}
	
	@PUT
	@Path("/{name}/operational_state")
	public Response updateOperationalState(@Valid @PathParam("name") ElementName name, 
										   OperationalState state){
		service.updateElementOperationalState(name,state);
		return success(messages);
	}
		
}
