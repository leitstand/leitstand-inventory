/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
