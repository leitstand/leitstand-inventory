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


import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_METRIC;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.MetricExportService;
import io.leitstand.inventory.service.MetricsExport;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_METRIC})
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
	@Scopes({IVT, IVT_READ, IVT_METRIC})
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
