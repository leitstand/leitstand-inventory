/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public interface ElementGroupSettingsService {

	boolean storeElementGroupSettings(ElementGroupSettings settings);

	ElementGroupSettings getGroupSettings(ElementGroupId id);

	ElementGroupSettings getGroupSettings(ElementGroupType groupType,
										  ElementGroupName groupName);

	void remove(ElementGroupId id);

	void remove(ElementGroupType groupType,
				ElementGroupName groupName);

}