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
package io.leitstand.inventory.rs;

import static io.leitstand.commons.UniqueKeyConstraintViolationException.key;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT_SETTINGS;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static io.leitstand.inventory.service.ReasonCode.IVT0307E_ELEMENT_NAME_ALREADY_IN_USE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.ElementSettingsService;
import io.leitstand.security.auth.Scopes;

/**
 * Manages the general settings of an element.
 */
@Resource
@Scopes({IVT, IVT_ELEMENT,IVT_ELEMENT_SETTINGS})
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementSettingsResource{

	@Inject
	private ElementSettingsService service;
	
	@Inject
	private Messages messages;
	
	/**
	 * Loads the general settings of the specified element.
	 * @param element the element UUID
	 * @return the element settings
	 */
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/settings")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT,IVT_ELEMENT_SETTINGS})
	public ElementSettings getElementSettings(@Valid @PathParam("element") ElementId element){
		return service.getElementSettings(element);
	}

	/**
	 * Load the general settings of the specified element.
	 * @param element the element name
	 * @return the element settings
	 */
	@GET
	@Path("/{element}/settings")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT,IVT_ELEMENT_SETTINGS})
	public ElementSettings getElementSettings(@Valid @PathParam("element") ElementName element){
		return service.getElementSettings(element);
	}
	
	/**
	 * Stores the general settings of the specified element.
	 * Creates a new element if the specified element does not exist.
	 * @param element the element UUID
	 * @param settings the element settings
	 * @return a created response if a new element was created, a success response if the settings of an existing element have been updated
	 */
	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/settings")
	public Response storeElementSettings(@Valid @PathParam("element") ElementId element, 
	                                     @Valid ElementSettings settings){
		
		if (isDifferent(element, settings.getElementId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "element_id", 
												   element, 
												   settings.getElementId());
		}
		
		try {
			if(service.storeElementSettings(settings)){
				return created(messages,"/elements/%s/settings",element);
			}
			return success(messages);
		} catch (PersistenceException e) {
			throw resolveRootCause(e,settings.getElementName());
		}
	}
	
	
	/**
	 * Stores the general settings of the specified element.
	 * Creates a new element if the specified element does not exist.
	 * @param element the element UUID
	 * @param settings the element settings
	 * @return a created response if a new element was created, a success response if the settings of an existing element have been updated
	 */
	@PUT
	@Path("/{element}/settings")
	public Response storeElementSettings(@PathParam("element") ElementName element, 
										 @Valid ElementSettings settings){
		try {
			if(service.storeElementSettings(settings)){
				return created(messages,"/elements/%s/settings",element);
			}
			return success(messages);
		} catch (PersistenceException e) {
			throw resolveRootCause(e,element);
		}
	}
	
	
	/**
	 * Adds a new element or updates an existing element.
	 * @param settings the element settings
	 * @return a created response if a new element was created, a success response if the settings of an existing element have been updated
	 */
	@POST
	public Response storeElementSettings(@Valid ElementSettings settings){
		return storeElementSettings(settings.getElementId(),
							 		settings);
	}

	private PersistenceException resolveRootCause(PersistenceException p, ElementName elementName) {
		try {
			// A unique key constraint violation occurred if an element with the given name exists.
			service.getElementSettings(elementName);
			throw new UniqueKeyConstraintViolationException(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE,
															key("element_name", elementName) );
		} catch(EntityNotFoundException e) {
			// Element does not exist. Continue with original exception
			throw p;
		}
	}

}