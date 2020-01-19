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

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.inventory.model.ElementRole.countElements;
import static io.leitstand.inventory.model.ElementRole.findRoles;
import static io.leitstand.inventory.service.ElementRoleSettings.newElementRoleSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0401I_ELEMENT_ROLE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0402I_ELEMENT_ROLE_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0403E_ELEMENT_ROLE_NOT_REMOVABLE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementRoleSettings;

@Dependent
public class ElementRoleManager  {

	private static final Logger LOG = Logger.getLogger(ElementRoleManager.class.getName());
	
	private Repository repository;
	
	private DatabaseService db; 
	
	private Messages messages;
	
	protected ElementRoleManager() {
		// CDI
	}
	
	@Inject
	protected ElementRoleManager(@Inventory Repository repository,
								 @Inventory DatabaseService db,
								 Messages messages) {
		this.repository = repository;
		this.db = db;
		this.messages = messages;
	}
	
	public List<ElementRoleSettings> getElementRoles() {
		return repository.execute(findRoles())
						 .stream()
						 .map(role -> roleSettings(role))
						 .collect(toList());
	}

	private ElementRoleSettings roleSettings(ElementRole role) {
		return newElementRoleSettings()
			   .withRoleId(role.getRoleId())
			   .withRoleName(role.getRoleName())
			   .withDisplayName(role.getDisplayName())
			   .withDescription(role.getDescription())
			   .withPlane(role.getPlane())
			   .withManageable(role.isManageable())
			   .build();
	}


	public ElementRoleSettings getElementRoleSettings(ElementRole role) {
		return roleSettings(role);
	}
	
	public void removeRole(ElementRole role) {
		long elementCount = repository.execute(countElements(role));
		if(elementCount > 0) {
			LOG.fine(()->format("%s: Role %s cannot be removed because %d instances of that role exists.",
								IVT0403E_ELEMENT_ROLE_NOT_REMOVABLE.getReasonCode(),
								role.getRoleName(),
								elementCount));
			
			throw new ConflictException(IVT0403E_ELEMENT_ROLE_NOT_REMOVABLE, 
									    role.getRoleName(),
									    elementCount);
		}
		repository.remove(role);
		LOG.fine(()->format("%s: Role %s removed.",
							IVT0402I_ELEMENT_ROLE_REMOVED,
							role.getRoleName()));
		messages.add(createMessage(IVT0402I_ELEMENT_ROLE_REMOVED,
								   role.getRoleName()));
	}
	
	public void storeElementRole(ElementRole role, ElementRoleSettings settings) {
		role.setRoleName(settings.getRoleName());
		role.setDisplayName(settings.getDisplayName());
		role.setDescription(settings.getDescription());
		role.setPlane(settings.getPlane());
		role.setManageable(settings.isManageable());
		LOG.fine(() -> format("%s: Stored role %s.",
							  IVT0401I_ELEMENT_ROLE_STORED,
							  settings.getRoleName()));
		messages.add(createMessage(IVT0401I_ELEMENT_ROLE_STORED, 
							       settings.getRoleName()));

		
	}

	public void createElementRole(ElementRoleSettings settings) {
		ElementRole role = new ElementRole(settings.getRoleId());
		repository.add(role);
		storeElementRole(role,settings);
	}

}
