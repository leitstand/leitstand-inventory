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
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

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
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementGroupExportService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementGroupsExport;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT_READ, IVT})
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
