/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.security.auth.Role.OPERATOR;
import static io.leitstand.security.auth.Role.SYSTEM;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.RetireElementService;

@RequestScoped
@Path("/elements")
public class RetireElementResource {

	@Inject
	private RetireElementService service;
	
	@Inject
	private Messages messages;
	
	@POST
	@Path("/{element:"+UUID_PATTERN+"}/_retire")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response retireElement(@PathParam("element") ElementId elementId) {
		service.retireElement(elementId);
		return success(messages);
	}
	
	@POST
	@Path("/{element}/_retire")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response retireElement(@PathParam("element") ElementName elementName) {
		service.retireElement(elementName);
		return success(messages);
	}
	
	
}
