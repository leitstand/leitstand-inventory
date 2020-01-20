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

/**
 * A service to export metric definitions and to import a metric export file
 */
public interface MetricExportService {

	/**
	 * Exports all metrics matching the given filter expression.
	 * @param filter - the filter expression
	 * @return all metrics matching the given filter or an empty report, if no matching metrics were found
	 */
	MetricsExport exportMetrics(String filter);
	
	/**
	 * Imports a metric export and synchronizes the metric definitions with all existing metrics.
	 * @param export - the export to be imported
	 */
	void importMetrics(MetricsExport export);
	
}
