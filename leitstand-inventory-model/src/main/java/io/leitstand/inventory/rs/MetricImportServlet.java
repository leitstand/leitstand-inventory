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
