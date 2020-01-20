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
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.security.auth.Role.OPERATOR;
import static io.leitstand.security.auth.Role.SYSTEM;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupRack;
import io.leitstand.inventory.service.ElementGroupRackService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RackSettings;



@RequestScoped
@Path("/{group_type}s")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementGroupRackResource {
	
	@Inject
	private ElementGroupRackService service;

	@Inject
	private Messages messages;
	
	@GET
	@Path("/{group_name}/racks/{rack_name}")
	public ElementGroupRack getRack(@PathParam("group_type") ElementGroupType groupType,
									@PathParam("group_name") ElementGroupName groupName,
									@PathParam("rack_name") RackName rackName){
		return service.getRack(groupType, 
							   groupName,
							   rackName);
	}
		

	@GET
	@Path("/{group_id:"+UUID_PATTERN+"}/racks/{rack_name}")
	public ElementGroupRack getRack(@PathParam("group_id") ElementGroupId groupId,
									@PathParam("rack_name") RackName rackName){
		
		return service.getRack(groupId, 
							   rackName);
	}
	
	@PUT
	@Path("/{group_name}/racks/{rack_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeRack(@PathParam("group_type") ElementGroupType groupType,
							  @PathParam("group_name") ElementGroupName groupName,
							  @PathParam("rack_name") RackName rackName,
							  RackSettings rack){
		if(service.storeRack(groupType, 
						     groupName,
						     rackName,
						     rack)) {
			return created(messages,rack.getRackName());
		}
			
		return success(messages);
	}
		
	@PUT
	@Path("/{group_id:"+UUID_PATTERN+"}/racks/{rack_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeRack(@PathParam("group_id") ElementGroupId groupId,
							  @PathParam("rack_name") RackName rackName,
							  RackSettings rack){
		
		if(service.storeRack(groupId,
							 rackName,
							 rack)) {

			return created(messages,rack.getRackName());
		}
		return success(messages);
	}
	
	@DELETE
	@Path("/{group_name}/racks/{rack_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeRack(@PathParam("group_type") ElementGroupType groupType,
							   @PathParam("group_name") ElementGroupName groupName,
							   @PathParam("rack_name") RackName rackName){
		service.removeRack(groupType, 
						   groupName,
						   rackName);
		return success(messages);
	}
	
		

	@DELETE
	@Path("/{group_id:"+UUID_PATTERN+"}/racks/{rack_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeRack(@PathParam("group_id") ElementGroupId groupId,
							   @PathParam("rack_name") RackName rackName){
		
		service.removeRack(groupId, 
					       rackName);
		return success(messages);
	}
	
}
