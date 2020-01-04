/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

import io.leitstand.inventory.service.MetricExportService;
import io.leitstand.inventory.service.MetricsExport;

@WebServlet(urlPatterns="/api/v1/import/metrics")
@MultipartConfig
public class MetricImportServlet extends BaseImportServlet<MetricsExport> {

	private static final long serialVersionUID = 1L;

	@Inject
	private MetricExportService inventory;
	
	@Override
	protected String getRedirectTarget() {
		return "/rbms/metric/metrics.html";
	}

	@Override
	protected Class<MetricsExport> getImportType() {
		return MetricsExport.class;
	}

	@Override
	protected void doImport(MetricsExport export) {
		inventory.importMetrics(export);
	}

}
