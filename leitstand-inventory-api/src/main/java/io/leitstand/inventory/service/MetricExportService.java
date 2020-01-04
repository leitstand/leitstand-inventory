/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
