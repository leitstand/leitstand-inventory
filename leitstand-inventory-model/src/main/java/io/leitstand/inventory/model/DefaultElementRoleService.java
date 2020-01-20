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
package io.leitstand.inventory.model;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementRoleId;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementRoleService;
import io.leitstand.inventory.service.ElementRoleSettings;

@Service
public class DefaultElementRoleService implements ElementRoleService {

	private ElementRoleManager manager;
	
	private ElementRoleProvider roles;

	protected DefaultElementRoleService() {
		// CDI
	}
	
	@Inject
	protected DefaultElementRoleService(ElementRoleProvider provider, ElementRoleManager manager) {
		this.manager = manager;
		this.roles = provider;
	}
	
	@Override
	public List<ElementRoleSettings> getElementRoles() {
		return manager.getElementRoles();
	}

	public ElementRoleSettings getElementRole(ElementRoleId roleId) {
		ElementRole role = roles.fetchElementRole(roleId);
		return manager.getElementRoleSettings(role);
	}
	
	@Override
	public void removeElementRole(ElementRoleName name) {
		ElementRole role = roles.tryFetchElementRole(name);
		if(role != null) {
			manager.removeRole(role);
		}
	}
	
	@Override
	public void removeElementRole(ElementRoleId roleId) {
		ElementRole role = roles.tryFetchElementRole(roleId);
		if(role != null) {
			manager.removeRole(role);
		}
	}

	public ElementRoleSettings getElementRole(ElementRoleName roleName) {
		ElementRole role = roles.fetchElementRole(roleName);
		return manager.getElementRoleSettings(role);
	}
	
	@Override
	public boolean storeElementRole(ElementRoleSettings settings) {
		ElementRole role = roles.tryFetchElementRole(settings.getRoleId());
		if(role != null) {
			manager.storeElementRole(role, settings);
			return false;
		}
		manager.createElementRole(settings);
		return true;
	}

}
