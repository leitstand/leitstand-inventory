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

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricScope;
import io.leitstand.inventory.service.MetricSettings;
import io.leitstand.inventory.service.MetricSettingsService;

@Service
public class DefaultMetricSettingsService implements MetricSettingsService{

	@Inject
	private MetricSettingsManager manager;
	
	@Inject
	private MetricProvider metrics;
	
	public DefaultMetricSettingsService() {
		// CDI
	}
	
	DefaultMetricSettingsService(MetricSettingsManager manager,
								 MetricProvider metrics) {
		this.manager = manager;
		this.metrics = metrics;
	}

	@Override
	public void removeMetric(MetricName metricName) {
		Metric metric = metrics.tryFetchMetric(metricName);
		if(metric != null) {
			manager.removeMetric(metric);
		}
	}
	
	@Override
	public void forceRemoveMetric(MetricName metricName) {
		Metric metric = metrics.tryFetchMetric(metricName);
		if(metric != null){
			manager.forceRemoveMetric(metric);
		}
	}


	@Override
	public List<MetricSettings> findMetrics(String filter) {
		return manager.findMetrics(filter, null);
	}

	@Override
	public List<MetricSettings> findMetrics(String filter, 
											MetricScope scope) {
		return manager.findMetrics(filter, scope);
	}

	@Override
	public MetricSettings getMetricSettings(MetricName metricName) {
		Metric metric = metrics.fetchMetric(metricName);
		return manager.getMetricSettings(metric);
	}

	@Override
	public boolean storeMetricSettings(MetricSettings settings) {
		Metric metric = metrics.tryFetchMetric(settings.getMetricId());
		if(metric == null) {
			manager.createMetric(settings);
			return true;
		}
		manager.storeMetric(metric,settings);
		return false;
	}

	@Override
	public void removeMetric(MetricId metricId) {
		Metric metric = metrics.tryFetchMetric(metricId);
		if(metric != null) {
			manager.removeMetric(metric);
		}
		
	}

	@Override
	public void forceRemoveMetric(MetricId metricId) {
		Metric metric = metrics.tryFetchMetric(metricId);
		if(metric != null) {
			manager.forceRemoveMetric(metric);
		}
	}

	@Override
	public MetricSettings getMetricSettings(MetricId metricId) {
		Metric metric = metrics.fetchMetric(metricId);
		return manager.getMetricSettings(metric);
	}

}
