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
package io.leitstand.inventory.model;

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.inventory.model.Element_Metric.countElementMetricBindings;
import static io.leitstand.inventory.model.Element_Metric.removeElementMetricBindings;
import static io.leitstand.inventory.model.Metric.findMetricsByName;
import static io.leitstand.inventory.service.MetricSettings.newMetricSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0601I_METRIC_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0602I_METRIC_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0603E_CANNOT_REMOVE_BOUND_METRIC;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.MetricScope;
import io.leitstand.inventory.service.MetricSettings;

@Dependent
public class MetricSettingsManager {

	private static final Logger LOG = Logger.getLogger(MetricSettingsManager.class.getName());
	
	private Repository repository;

	private ElementRoleProvider roles;
	
	private Messages messages;
	
	protected MetricSettingsManager() {
		//CDI
	}
	
	@Inject
	protected MetricSettingsManager(@Inventory Repository repository,
							ElementRoleProvider roles,
							Messages messages) {
		this.repository = repository;
		this.roles = roles;
		this.messages = messages;
	}
	

	public List<MetricSettings> findMetrics(String filter, 
											MetricScope scope) {
		return repository.execute(findMetricsByName(filter, scope))
						 .stream()
						 .map(metric -> newMetricSettings()
								 		.withMetricName(metric.getMetricName())
								 		.withMetricScope(metric.getMetricScope())
								 		.withMetricUnit(metric.getMetricUnit())
								 		.withDescription(metric.getDescription())
								 		.withElementRoles(metric.getElementRoles()
								 							    .stream()
								 							    .map(ElementRole::getRoleName)
								 							    .collect(toList()))
								 		.build())
						.collect(toList());
	}

	public MetricSettings getMetricSettings(Metric metric) {
		return newMetricSettings()
			   .withMetricId(metric.getMetricId())
			   .withMetricName(metric.getMetricName())
			   .withMetricScope(metric.getMetricScope())
			   .withMetricUnit(metric.getMetricUnit())
			   .withDescription(metric.getDescription())
			   .withElementRoles(metric.getElementRoles()
					   				   .stream()
					   				   .map(ElementRole::getRoleName)
					   				   .collect(toList()))
			   .build();
	}
	
	public void storeMetric(Metric metric, MetricSettings settings) {
		metric.setMetricName(settings.getMetricName());
		metric.setMetricScope(settings.getMetricScope());
		metric.setMetricUnit(settings.getMetricUnit());
		metric.setDescription(settings.getDescription());
		
		metric.setElementRoles(settings.getElementRoles()
									   .stream()
									   .map(role -> roles.fetchElementRole(role))
									   .collect(toSet()));
		
		
		LOG.fine(() -> format("Stored metric %s (%s) with scope %s",
							   settings.getMetricName(),
							   settings.getMetricId(),
							   settings.getMetricScope()));
		messages.add(createMessage(IVT0601I_METRIC_STORED, 
								   settings.getMetricName()));
	}

	public void createMetric(MetricSettings settings) {
		Metric metric = new Metric(settings.getMetricId(),settings.getMetricName());
		repository.add(metric);
		storeMetric(metric,settings);
	}
	
	
	public void removeMetric(Metric metric) {
		// Count metric references
		long count = repository.execute(countElementMetricBindings(metric));
		if(count > 0) {
			LOG.fine(() -> format("%s: Cannot remove %s metric. It is bound to %d elements.",
								  IVT0603E_CANNOT_REMOVE_BOUND_METRIC.getReasonCode(),
								  metric.getMetricName(),
								  count));
			throw new ConflictException(IVT0603E_CANNOT_REMOVE_BOUND_METRIC,
										metric.getMetricName(),
										count);
		}
		repository.remove(metric);
		LOG.fine(() -> format("%s: %s metric removed",
							  IVT0602I_METRIC_REMOVED.getReasonCode(),
							  metric.getMetricName()));
		
		messages.add(createMessage(IVT0602I_METRIC_REMOVED,
								   metric.getMetricName()));
		return;
	}
	
	public void forceRemoveMetric(Metric metric) {
		// Count metric references
		int count = repository.execute(removeElementMetricBindings(metric));
		
		
		LOG.fine(() -> format("%d %s metric bindings removed",
							  count,
							  metric.getMetricName()));
		
		repository.remove(metric);
		LOG.fine(() -> format("%s: %s metric removed",
							  IVT0602I_METRIC_REMOVED.getReasonCode(),
							  metric.getMetricName()));
		
		messages.add(createMessage(IVT0602I_METRIC_REMOVED, metric.getMetricName()));
		return;
	}

	
}
