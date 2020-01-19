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

import static io.leitstand.inventory.model.Metric.findMetricByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_METRIC_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;

@Dependent
class MetricProvider {
	
	private static final Logger LOG = Logger.getLogger(MetricProvider.class.getName());

	private Repository repository;

	@Inject
	protected MetricProvider(@Inventory Repository repository){
		this.repository = repository;
	}
	
	protected MetricProvider() {
		// CDI
	}

	public Metric tryFetchMetric(MetricName metricName) {
		return repository.execute(findMetricByName(metricName));
	}
	
	public Metric fetchMetric(MetricName metricName) {
		Metric metric = tryFetchMetric(metricName);
		if(metric == null) {
			LOG.fine(()-> format("%s: Requested metric %s does not exist",
								 IVT0600E_METRIC_NOT_FOUND.getReasonCode(),
								 metricName));
			throw new EntityNotFoundException(IVT0600E_METRIC_NOT_FOUND, 
											  metricName);
		}
		return metric;
	}
	
	public Metric tryFetchMetric(MetricId metricId) {
		return repository.execute(Metric.findMetricById(metricId));
	}
	

	public Metric fetchMetric(MetricId metricId) {
		Metric metric = tryFetchMetric(metricId);
		if(metric == null) {
			LOG.fine(()-> format("%s: Requested metric %s does not exist",
								 IVT0600E_METRIC_NOT_FOUND.getReasonCode(),
								 metricId));
			throw new EntityNotFoundException(IVT0600E_METRIC_NOT_FOUND, metricId);
		}
		return metric;
	}
	
}
