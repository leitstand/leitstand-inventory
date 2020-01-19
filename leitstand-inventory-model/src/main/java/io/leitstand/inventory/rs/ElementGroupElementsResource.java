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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.leitstand.inventory.service.ElementGroupElements;
import io.leitstand.inventory.service.ElementGroupElementsService;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.Plane;

@RequestScoped
@Path("/{group_type}s")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementGroupElementsResource {
	
	@Inject
	private ElementGroupElementsService service;
	
	
	@GET
	@Path("/{group_id:"+UUID_PATTERN+"}/elements")
	public ElementGroupElements getGroupElements(@Valid @PathParam("group_id") ElementGroupId id, 
	                                  @QueryParam("plane") Plane plane){
		return service.getGroupElements(id,plane);
	}
	
	@GET
	@Path("/{group_name}/elements")
	public ElementGroupElements getGroupElements(@Valid @PathParam("group_type") ElementGroupType groupType, 
												 @Valid @PathParam("group_name") ElementGroupName groupName, 
	                                  			 @QueryParam("plane") Plane plane){
		return service.getGroupElements(groupType,
										groupName,
										plane);
	}
	
}
