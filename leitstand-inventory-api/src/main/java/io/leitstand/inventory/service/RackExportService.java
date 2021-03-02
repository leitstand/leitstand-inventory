package io.leitstand.inventory.service;

/**
 * A service to export racks from and import racks to the inventory.
 */
public interface RackExportService {

    /**
     * Exports all racks with a matching name from the inventory.
     * @param filter a regular expression to match racks by name.
     * @return a rack export with all matching racks.
     */
	RackExport exportRacks(String filter);
	
	/**
	 * Imports all racks into the inventory. Existing racks are overwritten.
	 * @param racks the rack export to be imported in the inventory.
	 */
	void importRacks(RackExport racks);
	
}
