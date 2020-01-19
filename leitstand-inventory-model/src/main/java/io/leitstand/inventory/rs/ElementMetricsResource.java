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
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.security.auth.Role.OPERATOR;
import static io.leitstand.security.auth.Role.SYSTEM;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementMetric;
import io.leitstand.inventory.service.ElementMetricService;
import io.leitstand.inventory.service.ElementMetrics;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricScope;

@RequestScoped
@Path("/elements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementMetricsResource {

	@Inject
	private ElementMetricService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/metrics")
	public ElementMetrics getElementMetrics(@PathParam("element") ElementId elementId,
											@QueryParam("metric_scope") MetricScope scope) {
		return service.findElementMetrics(elementId,
										  scope);	
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/metrics/{metric}")
	public ElementMetric getElementMetric(@PathParam("element") ElementId elementId,
									      @PathParam("metric") MetricName metricName) {
		return service.getElementMetric(elementId,metricName);
	}
	
	@GET
	@Path("/{element}/metrics/{metric}")
	public ElementMetric getElementMetrics(@PathParam("element") ElementName elementName,
										   @PathParam("metric") MetricName metricName) {
		return service.getElementMetric(elementName,metricName);
	}
	
	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/metrics/{metric}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public void updateElementMetric(@PathParam("element") ElementId elementId,
								    @PathParam("metric") MetricName metricName,
								    ElementMetric metric) {
		service.storeElementMetric(elementId,metric);
	}
	
	@PUT
	@Path("/{element}/metrics/{metric}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public void updateElementMetric(@PathParam("element") ElementName elementName,
			   						@PathParam("metric") MetricName metricName,
			   						ElementMetric metric) {
		service.storeElementMetric(elementName,metric);
	}

	@PUT
	@POST
	@Path("/{element:"+UUID_PATTERN+"}/metrics")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response registerElementMetrics(@PathParam("element") ElementId elementId,
										   List<String> metrics) {
		service.registerElementMetrics(elementId, 
									   metrics.stream()
									   		  .map(MetricName::valueOf)
									   		  .collect(toList()));		
		return success(messages);
	}

	@POST
	@PUT
	@Path("/{element}/metrics")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response registerElementMetrics(@PathParam("element") ElementName elementName,
										  List<String> metrics) {
		service.registerElementMetrics(elementName, 
									   metrics.stream()
									   		  .map(MetricName::valueOf)
									   		  .collect(toList()));
		return success(messages);
	}
	
}
