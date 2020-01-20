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
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupRackService;
import io.leitstand.inventory.service.ElementGroupRacks;
import io.leitstand.inventory.service.ElementGroupType;

@RequestScoped
@Path("/{group_type}s")
@Produces(APPLICATION_JSON)
public class ElementGroupRacksResource {


	@Inject
	private ElementGroupRackService service;
	
	@GET
	@Path("/{group_name}/racks")
	public ElementGroupRacks getRacks(@PathParam("group_type") ElementGroupType groupType,
									   @PathParam("group_name") ElementGroupName groupName){
		return service.findRacks(groupType, 
								 groupName);
	}

	@GET
	@Path("/{group_id:"+UUID_PATTERN+"}/racks")
	public ElementGroupRacks getRacks(@PathParam("group_id") ElementGroupId groupId){
		return service.findRacks(groupId);
	}
	
}
