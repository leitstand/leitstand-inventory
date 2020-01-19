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