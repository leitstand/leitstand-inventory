/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

public interface ElementMetricService {

	ElementMetrics findElementMetrics(ElementId elementId, 
									  MetricScope scope);
	ElementMetrics findElementMetrics(ElementName elementName, 
									  MetricScope scope);
	ElementMetric getElementMetric(ElementId elementId, 
								   MetricName metricName);
	ElementMetric getElementMetric(ElementName elementName, 
								   MetricName metricName);
	void storeElementMetric(ElementName elementName, 
							ElementMetric metric);
	void storeElementMetric(ElementId elementId, 
							ElementMetric metric);
	void registerElementMetrics(ElementId elementId, 
								List<MetricName> metrics);
	void registerElementMetrics(ElementName elementName, 
								List<MetricName> metrics);

}
