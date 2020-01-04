/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.security.auth.Role.OPERATOR;
import static io.leitstand.security.auth.Role.SYSTEM;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementService;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.OperationalState;

@RequestScoped
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementResource {

	@Inject
	private ElementService service;
	
	@Inject
	private Messages messages;
	
	@GET
	public List<ElementSettings> findElement(@QueryParam("filter") @DefaultValue("") String filter,
	                                         @QueryParam("offset") @DefaultValue("0") int offset,
											 @QueryParam("limit")  @DefaultValue("100") int limit){
		return service.findElements(filter, offset, limit);										 
	}
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElement(@PathParam("element") ElementId elementId,
								  @QueryParam("force") boolean force) {
		if(force) {
			service.forceRemoveElement(elementId);
		} else {
			service.removeElement(elementId);
		}
		return success(messages);
	}

	@DELETE
	@Path("/{element}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElement(@PathParam("element") ElementName elementName,
							  	  @QueryParam("force") boolean force) {
		if(force) {
			service.forceRemoveElement(elementName);
		} else {
			service.removeElement(elementName);
		}
		return success(messages);
	}
	
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/operational_state")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response updateOperationalState(@Valid @PathParam("id") ElementId id, 
										   OperationalState state){
		service.updateElementOperationalState(id,state);
		return success(messages);
	}
	
	@PUT
	@Path("/{name}/operational_state")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response updateOperationalState(@Valid @PathParam("name") ElementName name, 
										   OperationalState state){
		service.updateElementOperationalState(name,state);
		return success(messages);
	}
		
}