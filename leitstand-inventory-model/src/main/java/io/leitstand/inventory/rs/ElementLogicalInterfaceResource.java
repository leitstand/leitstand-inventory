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

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementLogicalInterface;
import io.leitstand.inventory.service.ElementLogicalInterfaceService;
import io.leitstand.inventory.service.ElementLogicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementLogicalInterfaces;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_ELEMENT})
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementLogicalInterfaceResource {
	
	private static final String IFL_PATTERN = "[a-z0-9]+-(?:\\d+\\/?)+";
	
	@Inject
	private ElementLogicalInterfaceService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/logical_interfaces")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT})
	public ElementLogicalInterfaces getLogicalInterfaces(@Valid @PathParam("element") ElementId id,
														 @QueryParam("filter") String filter,
														 @QueryParam("offset") @DefaultValue("0") int offset,
														 @QueryParam("limit") @DefaultValue("100") int limit){
		return service.findLogicalInterfaces(id, 
											 filter,
											 offset, 
											 limit);
	}
	
	@GET
	@Path("/{element}/logical_interfaces")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT})
	public ElementLogicalInterfaces getLogicalInterfaces(@Valid @PathParam("element") ElementName name,
			 											 @QueryParam("filter") String filter,
			 											 @QueryParam("offset") @DefaultValue("0") int offset,
			 											 @QueryParam("limit") @DefaultValue("100") int limit){
		return service.findLogicalInterfaces(name, 
											 filter,
											 offset, 
											 limit);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/logical_interfaces/{ifl_name:"+IFL_PATTERN+"}")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT})
	public ElementLogicalInterface getLogicalInterface(@PathParam("element") ElementId id, 
	                                                   @PathParam("ifl_name") InterfaceName iflName){
		return service.getLogicalInterface(id,iflName);
	}
	
	@GET
	@Path("/{element}/logical_interfaces/{ifl_name:"+IFL_PATTERN+"}")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT})
	public ElementLogicalInterface getLogicalInterface(@PathParam("element") ElementName name,
	                                                   @PathParam("ifl_name") InterfaceName iflName){
		return service.getLogicalInterface(name,iflName);
	}

	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/logical_interfaces/{ifl_name:"+IFL_PATTERN+"}")
	public Response storeLogicalInterface(@PathParam("element") ElementId id, 
	                                      @PathParam("ifl_name") InterfaceName iflName,
	                                      ElementLogicalInterfaceSubmission ifl){
		
		if(isDifferent(iflName, ifl.getIflName())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "ifl_name",
												   iflName,
												   ifl.getIflName());
		}
		
		if(service.storeLogicalInterface(id, ifl)){
			return created(iflName);
		}
		return success(messages);
	}
	
	@PUT
	@Path("/{element}/logical_interfaces/{ifl_name:"+IFL_PATTERN+"}")
	public Response storeLogicalInterface(@PathParam("element") ElementName name, 
	                                      @PathParam("ifl_name") InterfaceName iflName,
	                                      ElementLogicalInterfaceSubmission ifl){
		if(isDifferent(iflName, ifl.getIflName())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "ifl_name",
												   iflName,
												   ifl.getIflName());
		}
		
		if(service.storeLogicalInterface(name, ifl)){
			return created(messages,iflName);
		}
		return success(messages);
	}
	
	@DELETE
	@Path("/{element}/logical_interfaces/{ifl_name:"+IFL_PATTERN+"}")
	public Response removeLogicalInterface(@PathParam("element") ElementName elementName, 
	                                       @PathParam("ifl_name") InterfaceName iflName){
		service.removeLogicalInterface(elementName, iflName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}/logical_interfaces/{ifl_name:"+IFL_PATTERN+"}")
	public Response removeLogicalInterface(@PathParam("element") ElementId elementId, 
	                                       @PathParam("ifl_name") InterfaceName iflName){
		service.removeLogicalInterface(elementId, iflName);
		return success(messages);
	}
	
}
