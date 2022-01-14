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


import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_IMAGE;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

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
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageExport;
import io.leitstand.inventory.service.ImageExportService;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.Version;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_READ, IVT_IMAGE})
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
		
		ImageExport export = inventory.exportImages(filter,
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
	public Response importImagesJson(ImageExport export) {
		inventory.importImages(export);
		return success(messages);
	}
	
}
