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

import io.leitstand.inventory.service.ElementGroupExportService;
import io.leitstand.inventory.service.ElementGroupsExport;

@WebServlet(urlPatterns= {"/api/v1/import/groups",
						  "/import/groups"})
@MultipartConfig()
public class ElementGroupImportServlet extends BaseImportServlet<ElementGroupsExport> {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ElementGroupExportService service;

	@Override
	protected Class<ElementGroupsExport> getImportType() {
		return ElementGroupsExport.class;
	}

	@Override
	protected String getRedirectTarget() {
		return "/rbms/inventory/groups.html";
	}

	@Override
	protected void doImport(ElementGroupsExport data) {
		service.importElementGroups(data);
	}
	
	
}
