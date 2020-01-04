/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;


import static io.leitstand.commons.rs.Responses.success;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageExportService;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.ImagesExport;
import io.leitstand.inventory.service.Version;

@RequestScoped
@Path("/export")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ImageExportResource {

	@Inject
	private ImageExportService inventory;

	@Inject
	private Messages messages;
	
	@GET
	@Path("/images")
	public Response exportImagesJson(@QueryParam("filter") @DefaultValue("") String filter,
			 						 @QueryParam("revision") @Valid Version revision,
			 						 @QueryParam("image_type") ImageType type,
			 						 @QueryParam("image_state") ImageState state,
			 						 @QueryParam("element_role") @Valid ElementRoleName role){
		
		ImagesExport export = inventory.exportImages(filter,
													 role,
													 type,
													 state,
													 revision);
		
		return ok(export)
			   .header("Content-Disposition", "attachment; filename=image-export.json")
			   .build();
	}
	
	@PUT
	@Path("/images")
	public Response importImagesJson(ImagesExport export) {
		inventory.importImages(export);
		return success(messages);
	}
	
}
