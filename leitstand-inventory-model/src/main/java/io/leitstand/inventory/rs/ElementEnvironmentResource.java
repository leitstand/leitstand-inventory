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
import static io.leitstand.commons.model.RollbackExceptionUtil.givenRollbackException;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT_CONFIG;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static io.leitstand.inventory.service.ReasonCode.IVT0392I_ELEMENT_ENVIRONMENT_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.RollbackExceptionUtil;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementEnvironment;
import io.leitstand.inventory.service.ElementEnvironmentService;
import io.leitstand.inventory.service.ElementEnvironments;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentName;
import io.leitstand.inventory.service.ReasonCode;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementEnvironmentResource {

	
	@Inject
	private ElementEnvironmentService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/environments/")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})	
	public ElementEnvironments getElementEnvironments(@Valid @PathParam("element") ElementId elementId) {
		return service.getElementEnvironments(elementId);
	}
	
	@GET
	@Path("/{element}/environments/")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})	
	public ElementEnvironments getElementEnvironments(@Valid @PathParam("element") ElementName elementName) {
		return service.getElementEnvironments(elementName);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/environments/{env}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementEnvironment getElementEnvironment(@Valid @PathParam("element") ElementId elementId,
													@Valid @PathParam("env") EnvironmentName environmentName) {
		return service.getElementEnvironment(elementId,
										     environmentName);
	}

	@GET
	@Path("/{element}/environments/{env}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementEnvironment getElementEnvironment(@Valid @PathParam("element") ElementName elementName,
													@Valid @PathParam("env") EnvironmentName environmentName) {
		return service.getElementEnvironment(elementName,
											 environmentName);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/environments/{env}/variables")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public JsonObject getElementEnvironmentVariables(@Valid @PathParam("element") ElementId elementId,
													 @Valid @PathParam("env") EnvironmentName environmentName) {
		return service.getElementEnvironment(elementId,
										     environmentName)
					  .getVariables();
	}

	@GET
	@Path("/{element}/environments/{env}/variables")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public JsonObject getElementEnvironmentVariables(@Valid @PathParam("element") ElementName elementName,
													 @Valid @PathParam("env") EnvironmentName environmentName) {
		return service.getElementEnvironment(elementName,
											 environmentName)
					  .getVariables();
	}
	
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/environments/{env:"+UUID_PATTERN+"}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementEnvironment getElementEnvironment(@Valid @PathParam("element") ElementId elementId,
													@Valid @PathParam("env") EnvironmentId environmentId) {
		ElementEnvironment env = service.getElementEnvironment(environmentId);
		if(isDifferent(elementId, env.getElementId())) {
			throw new ConflictException(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT, environmentId);
		}
		return env;
	}

	@GET
	@Path("/{element}/environments/{env:"+UUID_PATTERN+"}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementEnvironment getElementEnvironment(@Valid @PathParam("element") ElementName elementName,
													@Valid @PathParam("env") EnvironmentId environmentId) {
		ElementEnvironment env = service.getElementEnvironment(environmentId);
		if(isDifferent(elementName, env.getElementName())) {
			throw new ConflictException(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT, environmentId);
		}
		return env;
	}
	
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}/environments/{env:"+UUID_PATTERN+"}")
	public Response removeElementEnvironment(@Valid @PathParam("element") ElementId elementId,
											 @Valid @PathParam("env") EnvironmentId environmentId) {
		ElementEnvironment env = service.getElementEnvironment(environmentId);
		if(isDifferent(elementId, env.getElementId())) {
			throw new ConflictException(IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT, 
										environmentId);
		}
		service.removeElementEnvironment(environmentId);
		return success(messages);
	}

	@DELETE
	@Path("/{element}/environments/{env}")
	public Response removeElementEnvironment(@Valid @PathParam("element") ElementName elementName,
										   	 @Valid @PathParam("env") EnvironmentName environmentName) {
		service.removeElementEnvironment(elementName,
										 environmentName);
		return success(messages);
	}
	
	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/environments/{env:"+UUID_PATTERN+"}")
	public Response storeElementEnvironment(@Valid @PathParam("element") ElementId elementId,
											@Valid @PathParam("env") EnvironmentId environmentId,
											@Valid Environment env) {
		
		if(isDifferent(environmentId,env.getEnvironmentId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "environment_id", 
												   environmentId, 
												   env.getEnvironmentId());
		}
		
		boolean created = service.storeElementEnvironment(elementId, 
														  env);
		if(created) {
			return created(messages,environmentId);
		}
		return success(messages);
	}

	@PUT
	@Path("/{element}/environments/{env:"+UUID_PATTERN+"}")
	public Response storeElementEnvironment(@Valid @PathParam("element") ElementName elementName,
										   	@Valid @PathParam("env") EnvironmentId environmentId,
										   	@Valid Environment env) {
		if(isDifferent(environmentId,env.getEnvironmentId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "environment_id", 
												   environmentId, 
												   env.getEnvironmentId());
		}
		
		boolean created = service.storeElementEnvironment(elementName, 
														  env);
		if(created) {
			return created(messages,environmentId);
		}
		return success(messages);
	}
	
	@POST
	@Path("/{element}/environments")
	public Response storeElementEnvironment(@Valid @PathParam("element") ElementName elementName,
										   	@Valid Environment env) {
	    
	    try {
    		boolean created = service.storeElementEnvironment(elementName, 
    														  env);
    		if(created) {
    			return created(messages,env.getEnvironmentId());
    		}
    		return success(messages);
	    } catch(Exception e) {
	        givenRollbackException(e)
	        .whenEntityExists(() -> service.getElementEnvironment(elementName, env.getEnvironmentName()))
	        .thenThrow(new UniqueKeyConstraintViolationException(IVT0392I_ELEMENT_ENVIRONMENT_REMOVED,
	                                                             key("environment_name", env.getEnvironmentName())));
	        throw e;
	    }
	}

	@POST
	@Path("/{element:"+UUID_PATTERN+"}/environments")
	public Response storeElementEnvironment(@Valid @PathParam("element") ElementId elementId,
										   	@Valid Environment env) {
	    try {
    		boolean created = service.storeElementEnvironment(elementId, 
    														  env);
    		if(created) {
    			return created(messages,env.getEnvironmentId());
    		}
    		return success(messages);
	    } catch(Exception e) {
            givenRollbackException(e)
            .whenEntityExists(() -> service.getElementEnvironment(elementId, env.getEnvironmentName()))
            .thenThrow(new UniqueKeyConstraintViolationException(IVT0392I_ELEMENT_ENVIRONMENT_REMOVED,
                                                                 key("environment_name", env.getEnvironmentName())));
            throw e;
        }
	}

	
}
