/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.Responses.success;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRack;
import io.leitstand.inventory.service.ElementRackLocation;
import io.leitstand.inventory.service.ElementRackService;

@RequestScoped
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementRackResource {


	@Inject
	private ElementRackService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/rack")
	public ElementRack getElementRack(@Valid @PathParam("id") ElementId id){
		return service.getElementRack(id);
	}
	
	@GET
	@Path("/{name}/rack")
	public ElementRack getElementRack(@Valid @PathParam("name") ElementName name){
		return service.getElementRack(name);
	}
	
	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/rack/")
	public Response setElementRackMountPoint(@Valid @PathParam("element") ElementId id,
											 ElementRackLocation mountPoint){
		service.storeElementRackMountPoint(id, 
										   mountPoint);
		return success(messages);
	}
	
	@PUT
	@Path("/{element}/rack")
	public Response setElementRackMountPoint(@Valid @PathParam("element") ElementName name,
									  		 ElementRackLocation mountPoint){
		service.storeElementRackLocation(name, 
										 mountPoint);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element}/rack")
	public Response removeElementRackMountPoint(@Valid @PathParam("element") ElementName elementName){
		service.removeElementRackLocation(elementName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}/rack")
	public Response removeElementRackMountPoint(@Valid @PathParam("element") ElementId elementId){
		service.removeElementRackLocation(elementId);
		return success(messages);
	}
		
}
