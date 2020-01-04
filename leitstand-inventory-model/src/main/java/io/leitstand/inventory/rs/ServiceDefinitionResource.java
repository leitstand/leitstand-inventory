/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.leitstand.inventory.service.ServiceDefinition;
import io.leitstand.inventory.service.ServiceDefinitionService;

@RequestScoped
@Path("/services")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ServiceDefinitionResource {

	@Inject
	private ServiceDefinitionService service;
	
	@GET
	public List<ServiceDefinition> getServices(){
		return service.getServices();
	}
	
	
	
}
