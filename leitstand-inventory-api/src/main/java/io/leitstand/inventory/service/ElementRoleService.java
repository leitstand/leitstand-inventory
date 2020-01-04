/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

public interface ElementRoleService {

	List<ElementRoleSettings> getElementRoles();
	boolean storeElementRole(ElementRoleSettings element);
	void removeElementRole(ElementRoleName roleName);
	void removeElementRole(ElementRoleId roleId);
	ElementRoleSettings getElementRole(ElementRoleId roleId);
	ElementRoleSettings getElementRole(ElementRoleName roleName);

	
}
