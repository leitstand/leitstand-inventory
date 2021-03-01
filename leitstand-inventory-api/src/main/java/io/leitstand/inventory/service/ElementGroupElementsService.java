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
 * A transactional service to access the elements of an element group.
 */
public interface ElementGroupElementsService {

    /**
     * Returns the element settings of all elements in the specified element group.
     * @param groupId the group ID
     * @return the element settings of all elements in the specified element group.
     */
	ElementGroupElements getGroupElements(ElementGroupId groupId);

	/**
	 * Returns the element settings of all elements in the specified element group and plane.
	 * @param groupId the group ID
	 * @param plane the plane
	 * @return the element settings of all elements in the specified element group and plane.
	 */
	ElementGroupElements getGroupElements(ElementGroupId groupid, 
										  Plane plane);

	
    /**
     * Returns the element settings of all elements in the specified element group.
     * @param groupType the group type
     * @param groupName the group name
     * @return the element settings of all elements in the specified element group.
     */
	ElementGroupElements getGroupElements(ElementGroupType groupType,
										  ElementGroupName groupName);

	/**
     * Returns the element settings of all elements in the specified element group and plane.
     * @param groupType the group type
     * @param groupName the group name
     * @param plane the plane
     * @return the element settings of all elements in the specified element group and plane.
     */
	ElementGroupElements getGroupElements(ElementGroupType groupType,
										  ElementGroupName groupName, 
										  Plane plane);
	
	/**
     * Returns the element settings of all elements in the specified element group with the specified role.
     * @param groupId the group ID
     * @param elementRole the element role
     * @return the element settings of all elements in the specified element group and element role.
     */
	ElementGroupElements getGroupElements(ElementGroupId groupId, 
										  ElementRoleName elementRole);
	
	
	/**
     * Returns the element settings of all elements in the specified element group with the specified role.
     * @param groupType the group type
     * @param groupName the group name
     * @param elementRole the element role
     * @return the element settings of all elements in the specified element group and element role.
     */
	ElementGroupElements getGroupElements(ElementGroupType groupType,
										  ElementGroupName groupName,
										  ElementRoleName elementRole);

}
