/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
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

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementServiceStack;
import io.leitstand.inventory.service.ElementServiceSubmission;
import io.leitstand.inventory.service.ElementServices;
import io.leitstand.inventory.service.ElementServicesService;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.ServiceName;

@RequestScoped
@Path("/elements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementServicesResource{

	
	@Inject
	private ElementServicesService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/services/{service_name}")
	public ElementServiceStack getElementServiceTopology(@Valid @PathParam("id") ElementId id, 
	                                                     @Valid @PathParam("service_name") ServiceName serviceName){
		return service.getElementServiceStack(id,serviceName);
	}

	
	@GET
	@Path("/{name}/services/{service_name}")
	public ElementServiceStack getElementServiceTopology(@Valid @PathParam("name") ElementName name, 
	                                                     @Valid @PathParam("service_name") ServiceName serviceName){
		return service.getElementServiceStack(name,serviceName);
	}
	
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/services")
	public ElementServices getServices(@Valid @PathParam("id") ElementId id){
		return service.getElementServices(id);
	}
		
	@GET
	@Path("/{name}/services")
	public ElementServices getServices(@Valid @PathParam("name") ElementName name){
		return service.getElementServices(name);
	}
	
	@DELETE
	@Path("/{id:"+UUID_PATTERN+"}/services/{service_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElementService(@Valid @PathParam("id") ElementId id, 
										 @Valid @PathParam("service_name") ServiceName serviceName){
		service.removeElementService(id,serviceName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{name}/services/{service_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElementService(@Valid @PathParam("name") ElementName name, 
	                                 	 @Valid @PathParam("service_name") ServiceName serviceName){
		service.removeElementService(name,serviceName);
		return success(messages);
	}

	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/services/{service_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
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
	@RolesAllowed({OPERATOR,SYSTEM})
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
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementServices(@Valid @PathParam("name") ElementName name,
										 @Valid List<ElementServiceSubmission> submissions) {
		service.storeElementServices(name, submissions);
		return success(messages);
	}
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/services/")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementServices(@Valid @PathParam("id") ElementId id,
										 @Valid List<ElementServiceSubmission> submissions) {
		service.storeElementServices(id, submissions);
		return success(messages);
	}
	
	@PUT
	@Path("/{name}/services/{service_name}/operational_state")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementService(@Valid @PathParam("name") ElementName name, 
	                                	@PathParam("service_name") ServiceName serviceName, 
	                                	@Valid OperationalState state){
		service.updateElementServiceOperationalState(name, serviceName, state);
		return success(messages);
	}
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/services/{service_name}/operational_state")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementService(@Valid @PathParam("id") ElementId id, 
	                                    @PathParam("service_name") ServiceName serviceName, 
	                                    @Valid OperationalState state){
		service.updateElementServiceOperationalState(id, serviceName, state);
		return success(messages);
	}
	
}
