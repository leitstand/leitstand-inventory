/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.inventory.event.ElementEnvironmentRemovedEvent.newElementEnvironmentRemovedEvent;
import static io.leitstand.inventory.event.ElementEnvironmentStoredEvent.newElementEnvironmentStoredEvent;
import static io.leitstand.inventory.model.Element_Environment.findEnvironmentById;
import static io.leitstand.inventory.model.Element_Environment.findEnvironmentByName;
import static io.leitstand.inventory.model.Element_Environment.findEnvironments;
import static io.leitstand.inventory.service.ElementEnvironment.newElementEnvironment;
import static io.leitstand.inventory.service.ElementEnvironments.newElementEnvironments;
import static io.leitstand.inventory.service.Environment.newEnvironment;
import static io.leitstand.inventory.service.EnvironmentInfo.newEnvironmentInfo;
import static io.leitstand.inventory.service.ReasonCode.IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0391I_ELEMENT_ENVIRONMENT_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0392I_ELEMENT_ENVIRONMENT_REMOVED;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.service.ElementEnvironment;
import io.leitstand.inventory.service.ElementEnvironments;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentInfo;
import io.leitstand.inventory.service.EnvironmentName;

@Dependent
public class ElementEnvironmentManager {
	private static final Logger LOG = Logger.getLogger(ElementEnvironmentManager.class.getName());

	private Repository repository;
	private Messages messages;
	private Event<ElementEvent> event;

	protected ElementEnvironmentManager() {
		// CDI
	}
	
	@Inject
	protected ElementEnvironmentManager(@Inventory Repository repository,
									    Event<ElementEvent> event,
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
		return newElementEnvironment()
			   .withGroupId(element.getGroupId())
			   .withGroupType(element.getGroupType())
			   .withGroupName(element.getGroupName())
			   .withElementRole(element.getElementRoleName())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withEnvironment(newEnvironment()
					   			.withEnvironmentId(env.getEnvironmentId())
					   			.withEnvironmentName(env.getEnvironmentName())
					   			.withCategory(env.getCategory())
					   			.withType(env.getType())
					   			.withDescription(env.getDescription())
					   			.withVariables(env.getVariables()))
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
		_env.setVariables(env.getVariables());
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
									   id));
			
			event.fire(newElementEnvironmentRemovedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupType(element.getGroupType())
					   .withGroupName(element.getGroupName())
					   .withElementRole(element.getElementRoleName())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
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
					   .withEnvironment(environmentInfo(env))
					   .build());
		}
	}

	public ElementEnvironments getElementEnvironments(Element element) {
		List<EnvironmentInfo> environments = repository.execute(findEnvironments(element))
													   .stream()
													   .map(env -> environmentInfo(env))
													   .collect(toList());
		
		return newElementEnvironments()
			   .withGroupId(element.getGroupId())
			   .withGroupType(element.getGroupType())
			   .withGroupName(element.getGroupName())
			   .withElementRole(element.getElementRoleName())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withEnvironments(environments)
			   .build();
	
	}
	
}