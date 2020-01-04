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
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementPhysicalInterface;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.ElementPhysicalInterfaceService;
import io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementPhysicalInterfaces;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.OperationalState;

@RequestScoped
@Path("/elements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementPhysicalInterfaceResource {
	
	@Inject
	private ElementPhysicalInterfaceService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces")
	public ElementPhysicalInterfaces getPhysicalInterfaces(@PathParam("element_id") ElementId id){
		return service.getPhysicalInterfaces(id);
	}
	
	@GET
	@Path("/{element_name}/physical_interfaces")
	public ElementPhysicalInterfaces getPhysicalInterfaces(@PathParam("element_name") ElementName name){
		return service.getPhysicalInterfaces(name);
	}
	
	@GET
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}")
	public ElementPhysicalInterface getPhysicalInterface(@PathParam("element_id") ElementId id, 
	                                                   @PathParam("ifp_name") InterfaceName ifpName){
		return service.getPhysicalInterface(id,ifpName);
	}
	
	@GET
	@Path("/{element_name}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}")
	public ElementPhysicalInterface getPhysicalInterface(@PathParam("element_name") ElementName name,
	                                                    @PathParam("ifp_name") InterfaceName ifpName){
		return service.getPhysicalInterface(name,ifpName);
	}
	
	@PUT
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicalInterface(@PathParam("element_id") ElementId id, 
	                                       @PathParam("ifp_name") InterfaceName ifpName,
	                                       ElementPhysicalInterfaceSubmission ifp){
	
		if(isDifferent(ifpName, ifp.getIfpName())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "ifp_name",
												   ifpName,
												   ifp.getIfpName());
		}
		
		if(service.storePhysicalInterface(id, ifp)){
			return created(ifpName);
		}
		return success(messages);
	}
	
	@PUT
	@Path("/{element_name}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}/neighbor")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicealInterfaceNeighbor(@PathParam("element_name") ElementName name, 
												@PathParam("ifp_name") InterfaceName ifpName,
												ElementPhysicalInterfaceNeighbor neighbor){
		service.storePhysicalInterfaceNeighbor(name,ifpName,neighbor);
		return success(messages);

	}
	
	@DELETE
	@Path("/{element_name}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removePhysicalInterface(@PathParam("element_name") ElementName name, 
	                              			@PathParam("ifp_name") InterfaceName ifpName){
		service.removePhysicalInterface(name,ifpName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removePhysicalInterface(@PathParam("element_id") ElementId id, 
											@PathParam("ifp_name") InterfaceName ifpName){
		service.removePhysicalInterface(id,ifpName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element_name}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}/neighbor")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removePhysicalInterfaceNeighbor(@PathParam("element_name") ElementName name, 
	                              					@PathParam("ifp_name") InterfaceName ifpName){
		service.removePhysicalInterfaceNeighbor(name,ifpName);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}/neighbor")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removePhysicalInterfaceNeighbor(@PathParam("element_id") ElementId id, 
													@PathParam("ifp_name") InterfaceName ifpName){
		service.removePhysicalInterfaceNeighbor(id,ifpName);
		return success(messages);
	}
	
	@PUT
	@Path("/{element_name}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}/operational_state")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicalLink(@PathParam("element_name") ElementName name, 
	                                  @PathParam("ifp_name") InterfaceName ifpName,
	                                  OperationalState opState){
		service.updatePhysicalInterfaceOperationalState(name,ifpName,opState);
		return success(messages);
	}
	
	@PUT
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}/operational_state")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicalLink(@PathParam("element_id") ElementId id, 
	                              	  @PathParam("ifp_name") InterfaceName ifpName,
	                              	  OperationalState opState){
		service.updatePhysicalInterfaceOperationalState(id,ifpName,opState);
		return success(messages);
	}
	
	@PUT
	@Path("/{element_name}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}/administrative_state")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicalLink(@PathParam("element_name") ElementName name, 
	                                  @PathParam("ifp_name") InterfaceName ifpName,
	                                  AdministrativeState admState){
		service.updatePhysicalInterfaceAdministrativeState(name,ifpName,admState);
		return success(messages);
	}
	
	
	@PUT
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}/administrative_state")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicalLink(@PathParam("element_id") ElementId id, 
	                                  @PathParam("ifp_name") InterfaceName ifpName,
	                                  AdministrativeState admState){
		service.updatePhysicalInterfaceAdministrativeState(id,ifpName,admState);
		return success(messages);
	}
	
	@PUT
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}/neighbor")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicalLink(@PathParam("element_id") ElementId id, 
	                                  @PathParam("ifp_name") InterfaceName ifpName,
	                                  ElementPhysicalInterfaceNeighbor neighbor){
		service.storePhysicalInterfaceNeighbor(id,ifpName,neighbor);
		return success(messages);
	}
	
	@PUT
	@Path("/{element_name}/physical_interfaces/{ifp_name:[a-z0-9]{2,4}-\\d+/\\d+/\\d+(:\\d+)?}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicalInterface(@PathParam("element_name") ElementName name, 
	                                       @PathParam("ifp_name") InterfaceName ifpName,
	                                       ElementPhysicalInterfaceSubmission ifp){
		if(isDifferent(ifpName, ifp.getIfpName())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "ifp_name",
												   ifpName,
												   ifp.getIfpName());
		}
		
		if(service.storePhysicalInterface(name, ifp)){
			return created(ifpName);
		}
		
		return success(messages);
	}
	
	@PUT
	@Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePhysicalInterfaces(@PathParam("element_id") ElementId id, 
											List<ElementPhysicalInterfaceSubmission> ifps){
		service.storePhysicalInterfaces(id, ifps);
		return success(messages);
	}
	
	@PUT
	@Path("/{element_name}/physical_interfaces")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storePyhsicalInterfaces(@PathParam("element_name") ElementName name, 
											List<ElementPhysicalInterfaceSubmission> ifps){
		service.storePhysicalInterfaces(name, ifps);
		return success(messages);
	}
	
}
