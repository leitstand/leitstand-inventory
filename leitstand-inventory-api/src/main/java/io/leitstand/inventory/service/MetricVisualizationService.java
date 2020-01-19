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
