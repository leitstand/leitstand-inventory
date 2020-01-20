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

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.security.auth.Role.OPERATOR;
import static io.leitstand.security.auth.Role.SYSTEM;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementModule;
import io.leitstand.inventory.service.ElementModuleService;
import io.leitstand.inventory.service.ElementModules;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ModuleData;
import io.leitstand.inventory.service.ModuleName;

@RequestScoped
@Path("/elements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementModulesResource{

	@Inject
	private ElementModuleService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/modules")
	public ElementModules getElementModules(@Valid @PathParam("id") ElementId id){
		return service.getElementModules(id);
	}

	@GET
	@Path("/{id:"+UUID_PATTERN+"}/modules/{module}")
	public ElementModule getElementModule(@Valid @PathParam("id") ElementId id, 
										  @PathParam("module") String module){
		try {
			return service.getElementModule(id,module);
		} catch (EntityNotFoundException e) {
			return service.getElementModule(id, ModuleName.valueOf(module));
		}
	}
	
	@GET
	@Path("/{name}/modules")
	public ElementModules getElementModules(@PathParam("name") ElementName name){
		return service.getElementModules(name);
	}
	
	@GET
	@Path("/{name}/modules/{module}")
	public ElementModule getElementModule(@Valid @PathParam("name") ElementName name, 
										  @PathParam("module") String module){
		try {
			return service.getElementModule(name,module);
		} catch (EntityNotFoundException e) {
			return service.getElementModule(name, ModuleName.valueOf(module));
		}
	}
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/modules/{module}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementModule(@Valid @PathParam("id") ElementId elementId, 
	                                   @PathParam("module") ModuleName moduleName,
	                                   ModuleData module){
		if(service.storeElementModule(elementId, moduleName, module)){
			return created(messages,moduleName);
		}
		return success(messages);
	}

	@PUT
	@Path("/{name}/modules/{module}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementModule(@Valid @PathParam("name") ElementName elementName, 
									   @Valid @PathParam("module") ModuleName moduleName,
	                                   @Valid ModuleData module){
		if(service.storeElementModule(elementName, moduleName, module)){
			return created(messages,moduleName);
		}
		return success(messages);
	}
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/modules")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementModule(@Valid @PathParam("id") ElementId elementId, 
									   @Valid List<ModuleData> modules){
		service.storeElementModules(elementId, 
									modules);
		return success(messages);
	}
	
	@PUT
	@Path("/{name}/modules")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementModules(@Valid @PathParam("name") ElementName elementName, 
	                                    @Valid List<ModuleData> modules){
		service.storeElementModules(elementName, 
									modules);
		return success(messages);
	}

	@DELETE
	@Path("/{id:"+UUID_PATTERN+"}/modules/{module}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElementModule(@Valid @PathParam("id") ElementId elementId, 
	                               		@Valid @PathParam("module") ModuleName moduleName){
		service.removeElementModule(elementId, 
									moduleName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{name}/modules/{module}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElementModule(@Valid @PathParam("name") ElementName elementName, 
	                               	@Valid @PathParam("module") ModuleName moduleName){
		service.removeElementModule(elementName,
									moduleName);
		return success(messages);

	}
	
}
