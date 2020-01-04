/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupRack;
import io.leitstand.inventory.service.ElementGroupRackService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.RackName;

@RequestScoped
@Path("/{group_type}")
public class RackSettingsResource {

	
	@Inject
	private ElementGroupRackService service;
	
	
	
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
	
	
}
