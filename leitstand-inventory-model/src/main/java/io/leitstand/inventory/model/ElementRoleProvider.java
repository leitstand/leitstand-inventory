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

import static io.leitstand.inventory.model.ElementRole.findRoleById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementRoleId;
import io.leitstand.inventory.service.ElementRoleName;

@Dependent
public class ElementRoleProvider {

	private static final Logger LOG = Logger.getLogger(ElementRoleProvider.class.getName());
	
	private Repository repository;
	
	@Inject
	protected ElementRoleProvider(@Inventory Repository repository) {
		this.repository = repository;
	}
	
	protected ElementRoleProvider() {
		// CDI
	}
	
	public ElementRole tryFetchElementRole(ElementRoleId roleId) {
		return repository.execute(findRoleById(roleId));
	}
	
	public ElementRole tryFetchElementRole(ElementRoleName roleName) {
		return repository.execute(findRoleByName(roleName));

	}
	
	public ElementRole fetchElementRole(ElementRoleId roleId) {
		ElementRole role = tryFetchElementRole(roleId);
		if(role == null) {
			LOG.fine(()-> format("%s: Element role %s does not exist", 
								 IVT0400E_ELEMENT_ROLE_NOT_FOUND.getReasonCode(),
								 roleId));
	
			throw new EntityNotFoundException(IVT0400E_ELEMENT_ROLE_NOT_FOUND, 
											  roleId);
		}
		return role;
	}
	
	public ElementRole fetchElementRole(ElementRoleName roleName) {
		ElementRole role = tryFetchElementRole(roleName);
		if(role == null) {
			LOG.fine(()-> format("%s: Element role %s does not exist", 
								 IVT0400E_ELEMENT_ROLE_NOT_FOUND.getReasonCode(),
								 roleName));
	
			throw new EntityNotFoundException(IVT0400E_ELEMENT_ROLE_NOT_FOUND, 
											  roleName);
		}
		return role;
	}
	
}
