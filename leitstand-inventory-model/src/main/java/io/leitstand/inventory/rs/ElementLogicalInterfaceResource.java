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
import io.leitstand.inventory.service.ElementLogicalInterface;
import io.leitstand.inventory.service.ElementLogicalInterfaceService;
import io.leitstand.inventory.service.ElementLogicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementLogicalInterfaces;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.InterfaceName;

@RequestScoped
@Path("/elements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementLogicalInterfaceResource {
	
	@Inject
	private ElementLogicalInterfaceService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{element_id:"+UUID_PATTERN+"}/logical_interfaces")
	public ElementLogicalInterfaces getLogicalInterfaces(@PathParam("element_id") ElementId id){
		return service.getLogicalInterfaces(id);
	}
	
	@GET
	@Path("/{element_name}/logical_interfaces")
	public ElementLogicalInterfaces getLogicalInterfaces(@PathParam("element_name") ElementName name){
		return service.getLogicalInterfaces(name);
	}
	
	@GET
	@Path("/{element_id:"+UUID_PATTERN+"}/logical_interfaces/{ifl_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+/\\d+/\\d+}")
	public ElementLogicalInterface getLogicalInterface(@PathParam("element_id") ElementId id, 
	                                                   @PathParam("ifl_name") InterfaceName iflName){
		return service.getLogicalInterface(id,iflName);
	}
	
	@GET
	@Path("/{element_name}/logical_interfaces/{ifl_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+}")
	public ElementLogicalInterface getLogicalInterface(@PathParam("element_name") ElementName name,
	                                                   @PathParam("ifl_name") InterfaceName iflName){
		return service.getLogicalInterface(name,iflName);
	}

	@PUT
	@Path("/{element_id:"+UUID_PATTERN+"}/logical_interfaces/{ifl_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+/\\d+/\\d+}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeLogicalInterface(@PathParam("element_id") ElementId id, 
	                                      @PathParam("ifl_name") InterfaceName iflName,
	                                      ElementLogicalInterfaceSubmission ifl){
		
		if(isDifferent(iflName, ifl.getIflName())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "ifl_name",
												   iflName,
												   ifl.getIflName());
		}
		
		if(service.storeLogicalInterface(id, ifl)){
			return created(iflName);
		}
		return success(messages);
	}
	
	@PUT
	@Path("/{element_name}/logical_interfaces/{ifl_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+/\\d+/\\d+}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeLogicalInterface(@PathParam("element_name") ElementName name, 
	                                      @PathParam("ifl_name") InterfaceName iflName,
	                                      ElementLogicalInterfaceSubmission ifl){
		if(isDifferent(iflName, ifl.getIflName())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "ifl_name",
												   iflName,
												   ifl.getIflName());
		}
		
		if(service.storeLogicalInterface(name, ifl)){
			return created(messages,iflName);
		}
		return success(messages);
	}
	
	@DELETE
	@Path("/{element_name}/logical_interfaces/{ifl_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+/\\d+/\\d+}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeLogicalInterface(@PathParam("element_name") ElementName elementName, 
	                                       @PathParam("ifl_name") InterfaceName iflName){
		service.removeLogicalInterface(elementName, iflName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element_id:"+UUID_PATTERN+"}/logical_interfaces/{ifl_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+/\\d+/\\d+}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeLogicalInterface(@PathParam("element_id") ElementId elementId, 
	                                       @PathParam("ifl_name") InterfaceName iflName){
		service.removeLogicalInterface(elementId, iflName);
		return success(messages);
	}
	
	@PUT
	@Path("/{element_id:"+UUID_PATTERN+"}/logical_interfaces")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeLogicalInterfaces(@PathParam("element_id") ElementId id, 
	                                   	   List<ElementLogicalInterfaceSubmission> ifls){
		service.storeLogicalInterfaces(id, ifls);
		return success(messages);
	}
	
	@PUT
	@Path("/{element_name}/logical_interfaces")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeLogicalInterfaces(@PathParam("element_name") ElementName name, 
										   List<ElementLogicalInterfaceSubmission> ifls){
		service.storeLogicalInterfaces(name, ifls);
		return success(messages);
	}
	
}
