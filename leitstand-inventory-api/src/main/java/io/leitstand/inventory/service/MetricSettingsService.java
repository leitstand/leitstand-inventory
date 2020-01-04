/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

public interface MetricSettingsService {
	
	List<MetricSettings> findMetrics(String filter);
	List<MetricSettings> findMetrics(String filter, 
									 MetricScope scope);
	
	MetricSettings getMetricSettings(MetricId metricId);
	MetricSettings getMetricSettings(MetricName metricName);
	
	void forceRemoveMetric(MetricId metricId);
	void forceRemoveMetric(MetricName metricName);

	void removeMetric(MetricId metricId);
	
	void removeMetric(MetricName metricName);
	
	boolean storeMetricSettings(MetricSettings settings);

}
