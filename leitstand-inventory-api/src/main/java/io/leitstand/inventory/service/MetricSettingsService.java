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
