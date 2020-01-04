/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
