/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.leitstand.inventory.service.PhysicalInterfaceData;
import io.leitstand.inventory.service.PhysicalInterfaceService;

@RequestScoped
@Path("/physical_interfaces")
public class PhysicalInterfacesResource {
	
	
	@Inject
	private PhysicalInterfaceService ifps;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PhysicalInterfaceData> findPhysicalInterfaces(@QueryParam("filter") String filter){
		return ifps.findPhysicalInterfaces(filter);
	}

}
