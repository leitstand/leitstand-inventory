/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricVisualization;
import io.leitstand.inventory.service.MetricVisualizationService;
import io.leitstand.inventory.service.MetricVisualizations;
import io.leitstand.inventory.service.VisualizationConfig;
import io.leitstand.inventory.service.VisualizationConfigId;
import io.leitstand.inventory.service.VisualizationConfigName;

@Service
public class DefaultMetricVisualizationService implements MetricVisualizationService{

	@Inject
	private MetricVisualizationManager inventory;
	
	@Inject
	private MetricProvider metrics;
	
	public DefaultMetricVisualizationService() {
		// CDI
	}
	
	DefaultMetricVisualizationService(MetricVisualizationManager manager,
						 MetricProvider metrics) {
		this.inventory = manager;
		this.metrics = metrics;
	}

	@Override
	public void removeMetricVisualization(MetricName metricName, 
										  VisualizationConfigName visualizationName) {
		Metric metric = metrics.fetchMetric(metricName);
		inventory.removeVisualization(metric,
									  visualizationName);
	}

	@Override
	public MetricVisualizations getMetricVisualizations(MetricName metricName) {
		Metric metric = metrics.fetchMetric(metricName);
		return inventory.getMetricVisualizations(metric);
	}

	@Override
	public MetricVisualization getMetricVisualization(MetricName metricName,
													  VisualizationConfigName visualizationName) {
		Metric metric = metrics.fetchMetric(metricName);
		return inventory.getMetricVisualization(metric,
												visualizationName);
	}


	@Override
	public MetricVisualizations getMetricVisualizations(MetricId metricId) {
		Metric metric = metrics.fetchMetric(metricId);
		return inventory.getMetricVisualizations(metric);
	}

	@Override
	public MetricVisualization getMetricVisualization(MetricId metricId, 
													  VisualizationConfigName visualizationName) {
		Metric metric = metrics.fetchMetric(metricId);
		return inventory.getMetricVisualization(metric, visualizationName);
	}

	@Override
	public boolean storeMetricVisualization(MetricId metricId, 
											VisualizationConfig visualization) {
		Metric metric = metrics.fetchMetric(metricId);
		return inventory.storeVisualization(metric, 
											visualization);
	}

	@Override
	public void removeMetricVisualization(MetricId metricId, 
										  VisualizationConfigName visualizationName) {
		Metric metric = metrics.fetchMetric(metricId);
		inventory.removeVisualization(metric, 
									  visualizationName);	
		
	}

	@Override
	public MetricVisualization getMetricVisualization(VisualizationConfigId visualizationId) {
		return inventory.getMetricVisualization(visualizationId);
	}

	@Override
	public void removeMetricVisualization(VisualizationConfigId visualizationId) {
		inventory.removeVisualization(visualizationId);
	}

	@Override
	public boolean storeMetricVisualization(MetricName metricName, VisualizationConfig visualizationConfig) {
		Metric metric = metrics.fetchMetric(metricName);
		return inventory.storeVisualization(metric, visualizationConfig);
	}
	
}
