/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public interface MetricVisualizationService {
	
	MetricVisualization getMetricVisualization(VisualizationConfigId visualizationId);
	MetricVisualization getMetricVisualization(MetricId metricId, 
										 	   VisualizationConfigName visualizationName);
	MetricVisualization getMetricVisualization(MetricName metricName, 
											   VisualizationConfigName visualizationName);

	boolean storeMetricVisualization(MetricId metricId, VisualizationConfig visualizationConfig);
	boolean storeMetricVisualization(MetricName metricName, VisualizationConfig visualizationConfig);
	
	
	MetricVisualizations getMetricVisualizations(MetricId metricId);
	MetricVisualizations getMetricVisualizations(MetricName metricName);

	void removeMetricVisualization(MetricId metricId,
							 	   VisualizationConfigName visualizationName);
	
	void removeMetricVisualization(MetricName metricName,
								   VisualizationConfigName visualizationName);

	void removeMetricVisualization(VisualizationConfigId visualizationId);

}
