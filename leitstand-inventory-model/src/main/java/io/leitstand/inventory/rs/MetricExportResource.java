/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.MetricExportService;
import io.leitstand.inventory.service.MetricsExport;

@RequestScoped
@Path("/export")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class MetricExportResource {

	@Inject
	private MetricExportService inventory;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/metrics")
	public Response exportMetricsJson(@QueryParam("filter") String filter){
		
		MetricsExport export = inventory.exportMetrics(filter);
		
		return ok(export)
			   .header("Content-Disposition", "attachment; filename=metric-export.json")
			   .build();
	}
	
	
	@PUT
	@Path("/metrics")
	public Messages importMetricsJson(MetricsExport export) {
		inventory.importMetrics(export);
		return messages;
	}
	
}
