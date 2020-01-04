/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;


import static io.leitstand.commons.rs.Responses.success;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementGroupExportService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementGroupsExport;

@RequestScoped
@Path("/export")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementGroupExportResource {

	@Inject
	private ElementGroupExportService inventory;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{group}s")
	public Response exportElementGroupsJson(@PathParam("group") ElementGroupType groupType,
											@QueryParam("filter") String filter){
		return ok(inventory.exportElementGroups(groupType,
												filter))
			   .header("Content-Disposition", "attachment; filename=inventory-export.json")
			   .build();
	}
	
	@PUT
	@Path("/{group}s")
	public Response importElementGroupsJson(@PathParam("group") ElementGroupType groupType, ElementGroupsExport export) {
		inventory.importElementGroups(export);
		return success(messages);
	}

}