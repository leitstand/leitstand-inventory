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

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementServiceStack;
import io.leitstand.inventory.service.ElementServiceSubmission;
import io.leitstand.inventory.service.ElementServices;
import io.leitstand.inventory.service.ElementServicesService;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.ServiceName;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_ELEMENT})
@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementServicesResource{

	
	@Inject
	private ElementServicesService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/services/{service_name}")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT})
	public ElementServiceStack getElementServiceTopology(@Valid @PathParam("id") ElementId id, 
	                                                     @Valid @PathParam("service_name") ServiceName serviceName){
		return service.getElementServiceStack(id,serviceName);
	}

	
	@GET
	@Path("/{name}/services/{service_name}")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT})
	public ElementServiceStack getElementServiceTopology(@Valid @PathParam("name") ElementName name, 
	                                                     @Valid @PathParam("service_name") ServiceName serviceName){
		return service.getElementServiceStack(name,serviceName);
	}
	
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/services")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT})
	public ElementServices getServices(@Valid @PathParam("id") ElementId id){
		return service.getElementServices(id);
	}
		
	@GET
	@Path("/{name}/services")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT})
	public ElementServices getServices(@Valid @PathParam("name") ElementName name){
		return service.getElementServices(name);
	}
	
	@DELETE
	@Path("/{id:"+UUID_PATTERN+"}/services/{service_name}")
	public Response removeElementService(@Valid @PathParam("id") ElementId id, 
										 @Valid @PathParam("service_name") ServiceName serviceName){
		service.removeElementService(id,serviceName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{name}/services/{service_name}")
	public Response removeElementService(@Valid @PathParam("name") ElementName name, 
	                                 	 @Valid @PathParam("service_name") ServiceName serviceName){
		service.removeElementService(name,serviceName);
		return success(messages);
	}

	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/services/{service_name}")
	public Response storeElementService(@Valid @PathParam("id") ElementId id, 
	                                    @Valid @PathParam("service_name") ServiceName serviceName, 
	                                    @Valid ElementServiceSubmission submission){
		if(isDifferent(serviceName, submission.getServiceName())){
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "service_name",
												   serviceName,
												   submission.getServiceName());
		}
		if(service.storeElementService(id,submission)){
			return created(messages,"/elements/%s/services/%s",id,serviceName);

		}
		return success(messages);
		
	}
	
	@PUT
	@Path("/{name}/services/{service_name}")
	public Response storeElementService(@Valid @PathParam("name") ElementName name, 
	                                    @PathParam("service_name") ServiceName serviceName, 
	                                    @Valid ElementServiceSubmission submission){
		if(isDifferent(serviceName, submission.getServiceName())){
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "service_name",
												   serviceName,
												   submission.getServiceName());
		}
		if(service.storeElementService(name,submission)){
			return created(messages,"/elements/%s/services/%s",name,serviceName);
		}
		return success(messages);	
	}
	
	@PUT
	@Path("/{name}/services/")
	public Response storeElementServices(@Valid @PathParam("name") ElementName name,
										 @Valid List<ElementServiceSubmission> submissions) {
		service.storeElementServices(name, submissions);
		return success(messages);
	}
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/services/")
	public Response storeElementServices(@Valid @PathParam("id") ElementId id,
										 @Valid List<ElementServiceSubmission> submissions) {
		service.storeElementServices(id, submissions);
		return success(messages);
	}
	
	@PUT
	@Path("/{name}/services/{service_name}/operational_state")
	public Response storeElementService(@Valid @PathParam("name") ElementName name, 
	                                	@PathParam("service_name") ServiceName serviceName, 
	                                	@Valid OperationalState state){
		service.updateElementServiceOperationalState(name, serviceName, state);
		return success(messages);
	}
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/services/{service_name}/operational_state")
	public Response storeElementService(@Valid @PathParam("id") ElementId id, 
	                                    @PathParam("service_name") ServiceName serviceName, 
	                                    @Valid OperationalState state){
		service.updateElementServiceOperationalState(id, serviceName, state);
		return success(messages);
	}
	
}
