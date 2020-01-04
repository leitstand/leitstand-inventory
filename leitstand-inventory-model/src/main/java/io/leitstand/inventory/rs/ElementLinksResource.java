/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
import javax.ws.rs.core.MediaType;

import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementLinkService;
import io.leitstand.inventory.service.ElementLinks;
import io.leitstand.inventory.service.ElementName;

@RequestScoped
@Path("/elements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
