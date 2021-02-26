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
package io.leitstand.inventory.service;

/**
 * The <code>ElementGroupSettingsService</code> allows managing the general group settings.
 */
public interface ElementGroupSettingsService {

    /**
     * Stores the general settings of an element group.
     * @param settings the general group settings.
     * @return <code>true</code> when a new element group was created and <code>false</code> if an existing group got updated.
     */
	boolean storeElementGroupSettings(ElementGroupSettings settings);

	/**
	 * Returns the general settings of the specified group.
	 * @param id the group ID
	 * @return the general group settings.
	 */
	ElementGroupSettings getGroupSettings(ElementGroupId id);

	/**
	 * Returns the general settings of the specified group.
	 * @param groupType the group type
	 * @param groupName the group name
	 * @return the general group settings
	 */
	ElementGroupSettings getGroupSettings(ElementGroupType groupType,
										  ElementGroupName groupName);

	/**
	 * Removes an empty element group. 
	 * An element group is empty if it contains no elements.
	 * @param groupId the group ID
	 */
	void remove(ElementGroupId groupId);

	/**
     * Removes an empty element group. 
     * An element group is empty if it contains no elements.
     * @param groupType the group type
     * @param groupName the group name
     */
	void remove(ElementGroupType groupType,
				ElementGroupName groupName);

}
