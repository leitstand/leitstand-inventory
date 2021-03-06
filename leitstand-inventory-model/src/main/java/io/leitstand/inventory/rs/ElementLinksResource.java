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
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementLinkService;
import io.leitstand.inventory.service.ElementLinks;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_READ, IVT_ELEMENT})
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementLinksResource{

	@Inject
	private ElementLinkService service;
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/links")
	public ElementLinks getElementLinks(@Valid @PathParam("id") ElementId id){
		return service.getElementLinks(id);
	}

	@GET
	@Path("/{name}/links")
	public ElementLinks getElementLinks(@Valid @PathParam("name") ElementName name){
		return service.getElementLinks(name);
	}
	
}
