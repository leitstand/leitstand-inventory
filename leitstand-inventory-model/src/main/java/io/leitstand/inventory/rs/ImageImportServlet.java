/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import javax.inject.Inject;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

import io.leitstand.inventory.service.ImageExportService;
import io.leitstand.inventory.service.ImagesExport;

@WebServlet(urlPatterns="/api/v1/import/images")
@MultipartConfig()
public class ImageImportServlet extends BaseImportServlet<ImagesExport>{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ImageExportService service;

	@Override
	protected Class<ImagesExport> getImportType() {
		return ImagesExport.class;
	}

	@Override
	protected String getRedirectTarget() {
		return "/rbms/image/images.html";
	}

	@Override
	protected void doImport(ImagesExport data) {
		service.importImages(data);
	}	
}
