package io.leitstand.inventory.service;

public interface RackExportService {

	RackExport exportRacks(String filter);
	void importRacks(RackExport racks);
	
}
