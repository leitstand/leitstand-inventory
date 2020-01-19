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


import static io.leitstand.commons.jsonb.IsoDateAdapter.isoDateFormat;
import static io.leitstand.commons.jsonb.IsoDateAdapter.parseIsoDate;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.AlertRule;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.MetricAlertRule;
import io.leitstand.inventory.service.MetricAlertRuleRevisions;
import io.leitstand.inventory.service.MetricAlertRuleService;
import io.leitstand.inventory.service.MetricAlertRules;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;

@RequestScoped
@Path("/metrics")
@Produces(APPLICATION_JSON)
public class MetricAlertRulesResource {

	@Inject
	private MetricAlertRuleService service;
	
	@Inject
	private Messages messages;
	

	@GET
	@Path("/{metric:"+UUID_PATTERN+"}/alertrules")
	public MetricAlertRules getMetricAlertRules(@PathParam("metric") @Valid MetricId metricId) {
		return service.getMetricAlertRules(metricId);
	}
	
	@GET
	@Path("/{metric_name}/alertrules")
	public MetricAlertRules getMetricAlertRules(@Valid @PathParam("metric_name") MetricName metricName) {
		return service.getMetricAlertRules(metricName);
	}
	
	@GET
	@Path("/{metric:"+UUID_PATTERN+"}/alertrules/{rule}")
	public MetricAlertRuleRevisions getMetricAlertRuleRevisions(@PathParam("metric") @Valid MetricId metricId,
																@PathParam("rule") @Valid AlertRuleName ruleName) {
		return service.getMetricAlertRuleRevisions(metricId,
												   ruleName);
	}
	
	@GET
	@Path("/{metric_name}/alertrules/{rule}")
	public MetricAlertRuleRevisions getMetricAlertRuleRevisions(@PathParam("metric_name") @Valid MetricName metricName,
																@PathParam("rule") @Valid AlertRuleName ruleName) {
		return service.getMetricAlertRuleRevisions(metricName,
												   ruleName);
	}
	
	@GET
	@Path("/{metric:"+UUID_PATTERN+"}/alertrules/{rule}/{dateModified}")
	public MetricAlertRule getMetricAlertRuleRevisions(@PathParam("metric") @Valid MetricId metricId,
													   @PathParam("rule") @Valid AlertRuleName ruleName,
													   @PathParam("dateModified") String dateModified) {
		return service.getMetricAlertRule(metricId,
										  ruleName,
										  parseIsoDate(dateModified));
	}
	
	@GET
	@Path("/{metric_name}/alertrules/{rule}/{dateModified}")
	public MetricAlertRule getMetricAlertRule(@PathParam("metric_name") @Valid MetricName metricName,
											  @PathParam("rule") @Valid AlertRuleName ruleName,
											  @PathParam("dateModified") String dateModified) {
		return service.getMetricAlertRule(metricName,
										  ruleName,
										  parseIsoDate(dateModified));
	}
	
	@DELETE
	@Path("/{metric:"+UUID_PATTERN+"}/alertrules/{rule}")
	public Response removeAlertRule(@PathParam("metric") @Valid MetricId metricId, 
									@PathParam("rule") @Valid AlertRuleName ruleName) {
		service.removeAlertRule(metricId, 
								ruleName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{metric}/alertrules/{rule}")
	public Response removeAlertRule(@PathParam("metric") @Valid MetricName metricName,
									@PathParam("rule") @Valid AlertRuleName ruleName) {
		service.removeAlertRule(metricName, 
								ruleName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{metric:"+UUID_PATTERN+"}/alertrules/{rule}/{dateModified}")
	public Response removeAlertRuleRevision(@PathParam("metric") @Valid MetricId metricId, 
											@PathParam("rule") @Valid AlertRuleName ruleName, 
											@PathParam("dateModified") @Valid String dateModified) {
		service.removeAlertRuleRevision(metricId, 
										ruleName, 
										parseIsoDate(dateModified));
		return success(messages);
	}
	
	@DELETE
	@Path("/{metric:"+UUID_PATTERN+"}/alertrules/{rule}/{dateModified}")
	public Response removeAlertRuleRevision(@PathParam("metric") @Valid MetricName metricName, 
											@PathParam("rule") @Valid AlertRuleName ruleName, 
											@PathParam("dateModified") String dateModified) {
		service.removeAlertRuleRevision(metricName, 
										ruleName, 
										parseIsoDate(dateModified));
		return success(messages);
	}
	
	@PUT
	@Path("/{metric:"+UUID_PATTERN+"}/alertrules/{rule}")
	public Response storeAlertRule(@PathParam("metric") @Valid MetricId metricId, 
								   @PathParam("rule") @Valid AlertRuleName ruleName, 
								   AlertRule rule) {
		boolean created = service.storeAlertRule(metricId, 
											     rule);
		if(created) {
			return created(messages,
						   "%s/%s",
						   rule.getRuleName(),
						   isoDateFormat(rule.getDateModified()));
		}
		return success(messages);
	}
	
	@PUT
	@Path("/{metric}/alertrules/{rule}")
	public Response storeAlertRule(@PathParam("metric") @Valid MetricName metricName, 
								   @PathParam("rule") @Valid AlertRuleName ruleName,
								   AlertRule rule) {
		boolean created = service.storeAlertRule(metricName, 
											     rule);
		if(created) {
			return created(messages,
						   "%s/%s",
						   rule.getRuleName(),
						   isoDateFormat(rule.getDateModified()));
		}
		return success(messages);
	}
	
	@POST
	@Path("/{metric:"+UUID_PATTERN+"}/alertrules")
	public Response storeAlertRule(@PathParam("metric") @Valid MetricId metricId, 
								   AlertRule rule) {
		boolean created = service.storeAlertRule(metricId,
												 rule);
		if(created) {
			return created(messages,
						   "%s/%s",
						   rule.getRuleName(),
						   isoDateFormat(rule.getDateModified()));
		}
		return success(messages);
	}
	
	@POST
	@Path("/{metric}/alertrules")
	public Response storeAlertRule(@PathParam("metric") @Valid MetricName metricName, 
								   AlertRule rule) {
		boolean created = service.storeAlertRule(metricName,
												 rule);
		if(created) {
			return created(messages,
						   "%s/%s",
						   rule.getRuleName(),
						   isoDateFormat(rule.getDateModified()));
		}
		return success(messages);
	}
	
	
}
