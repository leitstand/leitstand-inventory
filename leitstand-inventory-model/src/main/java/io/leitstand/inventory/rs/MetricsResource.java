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


import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_METRIC;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricScope;
import io.leitstand.inventory.service.MetricSettings;
import io.leitstand.inventory.service.MetricSettingsService;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_METRIC})
@Path("/metrics")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class MetricsResource {

	@Inject
	private MetricSettingsService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Scopes({IVT, IVT_READ, IVT_METRIC})
	public List<MetricSettings> findMetrics(@QueryParam("filter") String filter,
											@QueryParam("metric_scope") MetricScope scope){
		return service.findMetrics(filter,scope);
	}
	
	@GET
	@Path("/{metric:"+UUID_PATTERN+"}")
	@Scopes({IVT, IVT_READ, IVT_METRIC})
	public MetricSettings getMetricSettings(@PathParam("metric") @Valid MetricId metricId) {
		return service.getMetricSettings(metricId);
	}
	
	@GET
	@Path("/{metric_name}")
	@Scopes({IVT, IVT_READ, IVT_METRIC})
	public MetricSettings getMetricSettings(@Valid @PathParam("metric_name") MetricName metricName) {
		return service.getMetricSettings(metricName);
	}
		
	@PUT
	@Path("/{metric:"+UUID_PATTERN+"}")
	public Response storeMetricSettings(@Valid @PathParam("metric") MetricId metricId, 
										@Valid MetricSettings settings) {
		if(isDifferent(metricId, settings.getMetricId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "metric_id",
												   metricId, 
												   settings.getMetricId());
		}
		
		boolean created = service.storeMetricSettings(settings);
		if(created) {
			return created(messages,settings.getMetricId());
		}
		return success(messages);
	}
	
	@POST
	public Response storeMetricSettings(@Valid MetricSettings settings) {
		return storeMetricSettings(settings.getMetricId(),settings);
	}
	

	@DELETE
	@Path("/{metric}")
	public Response removeMetric(@PathParam("metric") @Valid MetricName metricName,
								 @QueryParam("force") boolean force) {
		if(force) {
			service.forceRemoveMetric(metricName);
		} else {
			service.removeMetric(metricName);
		}
		return success(messages);
	}
	
	@DELETE
	@Path("/{metric:"+UUID_PATTERN+"}")
	public Response removeMetric(@PathParam("metric") @Valid MetricId metricId,
								 @QueryParam("force") boolean force) {
		if(force) {
			service.forceRemoveMetric(metricId);
		} else {
			service.removeMetric(metricId);
		}
		return success(messages);
	}
	
}
