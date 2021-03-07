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

import java.util.List;

/**
 * A service for managing element roles.
 */
public interface ElementRoleService {

    /**
     * Returns all defined element roles.
     * @return a list of defined element roles.
     */
    List<ElementRoleSettings> getElementRoles();
    
    /**
     * Stores a element role. Returns <code>true</code> if a new element role is added, <code>false</code> otherwise.
     * @param role the element role settings
     * @return <code>true</code> if a new element role is added, <code>false</code> otherwise.
     */
	boolean storeElementRole(ElementRoleSettings role);
	
	/**
	 * Removes an element role.
	 * @param roleName the element role name.
	 */
	void removeElementRole(ElementRoleName roleName);
	
	/**
	 * Removes an element role.
	 * @param roleId the element role ID
	 */
	void removeElementRole(ElementRoleId roleId);
	
	/**
	 * Returns the element role settings.
	 * @param roleId the element role ID.
	 * @return the element role settings.
	 * @throws EntityNotFoundException if the element role does not exist.
	 */
	ElementRoleSettings getElementRole(ElementRoleId roleId);

	/**
     * Returns the element role settings.
     * @param roleName the element role name.
     * @return the element role settings.
     * @throws EntityNotFoundException if the element role does not exist.
     */
	ElementRoleSettings getElementRole(ElementRoleName roleName);

	
}
