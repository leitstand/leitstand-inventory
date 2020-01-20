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

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.leitstand.inventory.service.ElementGroupService;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupStatistics;
import io.leitstand.inventory.service.ElementGroupType;

@RequestScoped
@Path("/{group}s")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementGroupsResource {
	
	@Inject
	private ElementGroupService service;
	
	
	@GET
	public List<ElementGroupSettings> findGroups(@PathParam("group") ElementGroupType group,
												 @QueryParam("filter") @DefaultValue("") String filter,
            							 		 @QueryParam("offset") @DefaultValue("0") int offset,
            							 		 @QueryParam("limit") @DefaultValue("100") int limit){
		return service.findGroups(group,
								  filter, 
								  offset, 
								  limit);
	}
	
	@GET
	@Path("/_statistics")
	public List<ElementGroupStatistics> getGroupStatistics(@Valid @PathParam("group") ElementGroupType groupType,
			 											   @QueryParam("filter") @DefaultValue("") String filter){
		return service.getGroupStatistics(groupType,
										  filter);
	}

}
