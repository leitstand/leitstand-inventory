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
import static io.leitstand.commons.messages.Messages.errors;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.inventory.event.ElementEnvironmentRemovedEvent.newElementEnvironmentRemovedEvent;
import static io.leitstand.inventory.event.ElementEnvironmentStoredEvent.newElementEnvironmentStoredEvent;
import static io.leitstand.inventory.event.ElementEnvironmentUploadedEvent.newElementEnvironmentUploadedEvent;
import static io.leitstand.inventory.model.ElementValueObjects.elementValueObject;
import static io.leitstand.inventory.model.Element_Environment.findEnvironmentById;
import static io.leitstand.inventory.model.Element_Environment.findEnvironmentByName;
import static io.leitstand.inventory.model.Element_Environment.findEnvironments;
import static io.leitstand.inventory.service.ElementEnvironment.newElementEnvironment;
import static io.leitstand.inventory.service.ElementEnvironments.newElementEnvironments;
import static io.leitstand.inventory.service.EnvironmentInfo.newEnvironmentInfo;
import static io.leitstand.inventory.service.ReasonCode.IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0391I_ELEMENT_ENVIRONMENT_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0392I_ELEMENT_ENVIRONMENT_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0394E_ELEMENT_ENVIRONMENT_INVALID;
import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toList;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementEnvironmentEvent;
import io.leitstand.inventory.service.ElementEnvironment;
import io.leitstand.inventory.service.ElementEnvironments;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentInfo;
import io.leitstand.inventory.service.EnvironmentName;

@Dependent
public class ElementEnvironmentManager {
	private static final Logger LOG = getLogger(ElementEnvironmentManager.class.getName());

	private Repository repository;
	private Messages messages;
	private Event<ElementEnvironmentEvent<?>> event;

	protected ElementEnvironmentManager() {
		// CDI
	}
	
	@Inject
	protected ElementEnvironmentManager(@Inventory Repository repository,
										Event<ElementEnvironmentEvent<?>> event,
									    Messages messages) {
		this.repository = repository;
		this.messages = messages;
		this.event = event;
	}
	
	public ElementEnvironment getElementEnvironment(EnvironmentId id) {
		Element_Environment env = repository.execute(findEnvironmentById(id));
		if(env == null) {
			LOG.fine(()->format("%s: Element environment %s does not exist",
							    IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND.getReasonCode(),
							    id));
			throw new EntityNotFoundException(IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND, id);
		}
		return elementEnvironment(env);
	}
	
	public ElementEnvironment getElementEnvironment(Element element, EnvironmentName name) {
		Element_Environment env = repository.execute(findEnvironmentByName(element, name));
		if(env == null) {
			LOG.fine(()->format("%s: Element environment %s of %s %s (%s) does not exist",
							    IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND.getReasonCode(),
							    name,
							    element.getElementRoleName(),
							    element.getElementName(),
							    element.getElementId()));
			throw new EntityNotFoundException(IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND, 
											  name);
		}
		return elementEnvironment(env);
		
	}
	
	private ElementEnvironment elementEnvironment(Element_Environment env) {
		Element element = env.getElement();
		return elementValueObject(newElementEnvironment(),element)
   			   .withEnvironmentId(env.getEnvironmentId())
   			   .withEnvironmentName(env.getEnvironmentName())
   			   .withCategory(env.getCategory())
   			   .withType(env.getType())
   			   .withDescription(env.getDescription())
   			   .withVariables(env.getVariables())
			   .build();
	}
	
	private EnvironmentInfo environmentInfo(Element_Environment env) {
		return newEnvironmentInfo()
			   .withEnvironmentId(env.getEnvironmentId())
			   .withEnvironmentName(env.getEnvironmentName())
			   .withCategory(env.getCategory())
			   .withType(env.getType())
			   .withDescription(env.getDescription())
			   .build();
	}
	
	public boolean storeElementEnvironment(Element element, Environment env) {
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		boolean created = false;
		Element_Environment _env = repository.execute(findEnvironmentById(env.getEnvironmentId()));
		if(_env == null) {
			_env = new Element_Environment(element, 
										   env.getEnvironmentId(),
										   env.getEnvironmentName());
			repository.add(_env);
			created = true;
		} 
		
		// Notify validators about new environment.
		event.fire(newElementEnvironmentUploadedEvent()
				   .withGroupId(element.getGroupId())
				   .withGroupType(element.getGroupType())
				   .withGroupName(element.getGroupName())
				   .withElementRole(element.getElementRoleName())
				   .withElementId(element.getElementId())
				   .withElementName(element.getElementName())
				   .withElementAlias(element.getElementAlias())
				   .withAdministrativeState(element.getAdministrativeState())
				   .withOperationalState(element.getOperationalState())
				   .withDateModified(element.getDateModified())
				   .withEnvironment(env)
				   .build());
		
		if (messages.contains(errors())) {
			// Validation failed.
			throw new UnprocessableEntityException(IVT0394E_ELEMENT_ENVIRONMENT_INVALID, env.getEnvironmentName());
		}
		
		// Update variables first. 
		// The lazy loading of variables otherwise might replace all other applied property changes!
		// The described behavior does not occur in the integration tests. It is an observed runtime behavior!
		_env.setVariables(env.getVariables());
		
		Element owner = _env.getElement();
		if(isDifferent(element, owner)) {
			LOG.fine(()->format("Move environment %s (%s) from %s %s (%s) to %s %s (%s).",
								env.getEnvironmentName(),
								env.getEnvironmentId(),
								owner.getElementRoleName(),
								owner.getElementName(),
								owner.getElementId(),
								element.getElementRoleName(),
								element.getElementName(),
								element.getElementId()));
			repository.lock(owner, OPTIMISTIC_FORCE_INCREMENT);
			_env.setElement(owner);
			created = true;
		}
		_env.setEnvironmentName(env.getEnvironmentName());
		_env.setCategory(env.getCategory());
		_env.setType(env.getType());
		_env.setDescription(env.getDescription());
		LOG.fine(()->format("%s: Stored element environment %s for %s %s (%s)",
							IVT0391I_ELEMENT_ENVIRONMENT_STORED.getReasonCode(),
							env.getEnvironmentName(),
							element.getElementRoleName(),
							element.getElementName(),
							element.getElementId()));
		
		messages.add(createMessage(IVT0391I_ELEMENT_ENVIRONMENT_STORED, 
								   env.getEnvironmentName()));
		
		event.fire(newElementEnvironmentStoredEvent()
				   .withGroupId(element.getGroupId())
				   .withGroupType(element.getGroupType())
				   .withGroupName(element.getGroupName())
				   .withElementRole(element.getElementRoleName())
				   .withElementId(element.getElementId())
				   .withElementName(element.getElementName())
				   .withElementAlias(element.getElementAlias())
				   .withAdministrativeState(element.getAdministrativeState())
				   .withOperationalState(element.getOperationalState())
				   .withDateModified(element.getDateModified())
				   .withEnvironment(environmentInfo(_env))
				   .build());
		
		return created;
	}

	public void removeElementEnvironment(EnvironmentId id) {
		Element_Environment env = repository.execute(findEnvironmentById(id));
		if(env != null) {
			Element element = env.getElement();
			
			repository.remove(env);
			
			LOG.fine(() -> format("%s: Element environment %s removed from %s %s (%s).",
					  			  IVT0392I_ELEMENT_ENVIRONMENT_REMOVED.getReasonCode(),
					  			  id,
					  			  element.getElementRole(),
					  			  element.getElementName(),
					  			  element.getElementId()));		

			messages.add(createMessage(IVT0392I_ELEMENT_ENVIRONMENT_REMOVED, 
									   env.getEnvironmentName()));
			
			event.fire(newElementEnvironmentRemovedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupType(element.getGroupType())
					   .withGroupName(element.getGroupName())
					   .withElementRole(element.getElementRoleName())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withAdministrativeState(element.getAdministrativeState())
					   .withOperationalState(element.getOperationalState())
					   .withDateModified(element.getDateModified())
					   .withEnvironment(environmentInfo(env))
					   .build());
			
		}
	}

	public void removeElementEnvironment(Element element, EnvironmentName name) {
		Element_Environment env = repository.execute(findEnvironmentByName(element,name));
		if(env != null) {
			repository.remove(env);
			
			LOG.fine(() -> format("%s: Element environment %s removed from %s %s (%s).",
								  IVT0392I_ELEMENT_ENVIRONMENT_REMOVED.getReasonCode(),
								  name,
								  element.getElementRole(),
								  element.getElementName(),
								  element.getElementId()));		
			
			messages.add(createMessage(IVT0392I_ELEMENT_ENVIRONMENT_REMOVED, 
									   name));
			
			
			event.fire(newElementEnvironmentRemovedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupType(element.getGroupType())
					   .withGroupName(element.getGroupName())
					   .withElementRole(element.getElementRoleName())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withAdministrativeState(element.getAdministrativeState())
					   .withOperationalState(element.getOperationalState())
					   .withDateModified(element.getDateModified())
					   .withEnvironment(environmentInfo(env))
					   .build());
		}
	}

	public ElementEnvironments getElementEnvironments(Element element) {
		List<EnvironmentInfo> environments = repository.execute(findEnvironments(element))
													   .stream()
													   .map(env -> environmentInfo(env))
													   .collect(toList());
		
		return elementValueObject(newElementEnvironments(),element)
			   .withEnvironments(environments)
			   .build();
	
	}
	
}
