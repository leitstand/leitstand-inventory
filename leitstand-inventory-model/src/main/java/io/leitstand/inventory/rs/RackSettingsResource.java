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
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_GROUP;
import static io.leitstand.inventory.rs.Scopes.IVT_GROUP_SETTINGS;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupRack;
import io.leitstand.inventory.service.ElementGroupRackService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.RackName;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_GROUP, IVT_GROUP_SETTINGS})
@Path("/{group_type}")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class RackSettingsResource {

	
	@Inject
	private ElementGroupRackService service;
	
	
	
	@GET
	@Path("/{group_name}/racks/{rack_name}")
	@Scopes({IVT, IVT_READ, IVT_GROUP, IVT_GROUP_SETTINGS})
	public ElementGroupRack getRack(@PathParam("group_type") ElementGroupType groupType,
								    @PathParam("group_name") ElementGroupName groupName,
								    @PathParam("rack_name") RackName rackName){
		return service.getRack(groupType, 
							   groupName, 
							   rackName);
	}

	@GET
	@Path("/{group_id:"+UUID_PATTERN+"}/racks/{rack_name}")
	@Scopes({IVT, IVT_READ, IVT_GROUP, IVT_GROUP_SETTINGS})
	public ElementGroupRack getRack(@PathParam("group_id") ElementGroupId groupId,
							        @PathParam("rack_name") RackName rackName){
		return service.getRack(groupId, 
				   			   rackName);
	}
	
	
}
