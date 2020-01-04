/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

/**
 * A service to export all element groups and the general settings of their respective elements.
 */
public interface ElementGroupExportService {

	/**
	 * Exports all element groups matching the given filter expression.
	 * @param groupType - the type of group to export.
	 * @param filter - the filter expression
	 * @return all groups matching the given filter expression or an empty export, if no group matches.
	 */
	ElementGroupsExport exportElementGroups(ElementGroupType groupType,
											String filter);
	
	/**
	 * Imports a group export and synchronizes the element groups and their respective elements.
	 * @param export - the export to be implemented
	 */
	void importElementGroups(ElementGroupsExport export);
	
	
}
