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
import static io.leitstand.inventory.service.ElementPhysicalInterfaceFilter.ifpFilter;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
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
import io.leitstand.security.auth.Scopes;

@Resource
@Path("/elements")
@Scopes({IVT, IVT_ELEMENT})
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementPhysicalInterfaceResource {
    
    private static final String IFP_PATTERN = "[a-z0-9]+-(?:\\d+\\/?)+";

    
    @Inject
    private ElementPhysicalInterfaceService service;
    
    @Inject
    private Messages messages;
    
    @GET
    @Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces")
    @Scopes({IVT, IVT_READ, IVT_ELEMENT})
    public ElementPhysicalInterfaces getPhysicalInterfaces(@PathParam("element_id") ElementId id,
                                                           @QueryParam("operational_state") String opState,
                                                           @QueryParam("administrative_state") String admState,
                                                           @QueryParam("ifp_name") String ifpName,
                                                           @QueryParam("ifp_alias") String ifpAlias){
        return service.getPhysicalInterfaces(id, 
                                             ifpFilter()
                                             .administrativeState(admState)
                                             .operationalState(opState)
                                             .ifpNamePattern(ifpName)
                                             .ifpAliasPattern(ifpAlias) );
    }
    
    @GET
    @Path("/{element_name}/physical_interfaces")
    @Scopes({IVT, IVT_READ, IVT_ELEMENT})
    public ElementPhysicalInterfaces getPhysicalInterfaces(@PathParam("element_name") ElementName name,
                                                           @QueryParam("operational_state") String opState,
                                                           @QueryParam("administrative_state") String admState,
                                                           @QueryParam("ifp_name") String ifpName,
                                                           @QueryParam("ifp_alias") String ifpAlias){
        return service.getPhysicalInterfaces(name, 
                                             ifpFilter()
                                             .administrativeState(admState)
                                             .operationalState(opState)
                                             .ifpNamePattern(ifpName)
                                             .ifpAliasPattern(ifpAlias) );
    }
    
    @GET
    @Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}")
    @Scopes({IVT, IVT_READ, IVT_ELEMENT})
    public ElementPhysicalInterface getPhysicalInterface(@PathParam("element_id") ElementId id, 
                                                       @PathParam("ifp_name") InterfaceName ifpName){
        return service.getPhysicalInterface(id,ifpName);
    }
    
    @GET
    @Path("/{element_name}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}")
    @Scopes({IVT, IVT_READ, IVT_ELEMENT})
    public ElementPhysicalInterface getPhysicalInterface(@PathParam("element_name") ElementName name,
                                                        @PathParam("ifp_name") InterfaceName ifpName){
        return service.getPhysicalInterface(name,ifpName);
    }
    
    @PUT
    @Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}")
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
    @Path("/{element_name}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}/neighbor")
    public Response storePhysicealInterfaceNeighbor(@PathParam("element_name") ElementName name, 
                                                @PathParam("ifp_name") InterfaceName ifpName,
                                                ElementPhysicalInterfaceNeighbor neighbor){
        service.storePhysicalInterfaceNeighbor(name,ifpName,neighbor);
        return success(messages);

    }
    
    @DELETE
    @Path("/{element_name}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}")
    public Response removePhysicalInterface(@PathParam("element_name") ElementName name, 
                                              @PathParam("ifp_name") InterfaceName ifpName){
        service.removePhysicalInterface(name,ifpName);
        return success(messages);
    }
    
    @DELETE
    @Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}")
    public Response removePhysicalInterface(@PathParam("element_id") ElementId id, 
                                            @PathParam("ifp_name") InterfaceName ifpName){
        service.removePhysicalInterface(id,ifpName);
        return success(messages);
    }
    
    @DELETE
    @Path("/{element_name}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}/neighbor")
    public Response removePhysicalInterfaceNeighbor(@PathParam("element_name") ElementName name, 
                                                      @PathParam("ifp_name") InterfaceName ifpName){
        service.removePhysicalInterfaceNeighbor(name,ifpName);
        return success(messages);
    }
    
    @DELETE
    @Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}/neighbor")
    public Response removePhysicalInterfaceNeighbor(@PathParam("element_id") ElementId id, 
                                                    @PathParam("ifp_name") InterfaceName ifpName){
        service.removePhysicalInterfaceNeighbor(id,ifpName);
        return success(messages);
    }
    
    @PUT
    @Path("/{element_name}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}/operational_state")
    public Response storePhysicalLink(@PathParam("element_name") ElementName name, 
                                      @PathParam("ifp_name") InterfaceName ifpName,
                                      OperationalState opState){
        service.updatePhysicalInterfaceOperationalState(name,ifpName,opState);
        return success(messages);
    }
    
    @PUT
    @Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}/operational_state")
    public Response storePhysicalLink(@PathParam("element_id") ElementId id, 
                                        @PathParam("ifp_name") InterfaceName ifpName,
                                        OperationalState opState){
        service.updatePhysicalInterfaceOperationalState(id,ifpName,opState);
        return success(messages);
    }
    
    @PUT
    @Path("/{element_name}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}/administrative_state")
    public Response storePhysicalLink(@PathParam("element_name") ElementName name, 
                                      @PathParam("ifp_name") InterfaceName ifpName,
                                      AdministrativeState admState){
        service.updatePhysicalInterfaceAdministrativeState(name,ifpName,admState);
        return success(messages);
    }
    
    
    @PUT
    @Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}/administrative_state")
    public Response storePhysicalLink(@PathParam("element_id") ElementId id, 
                                      @PathParam("ifp_name") InterfaceName ifpName,
                                      AdministrativeState admState){
        service.updatePhysicalInterfaceAdministrativeState(id,ifpName,admState);
        return success(messages);
    }
    
    @PUT
    @Path("/{element_id:"+UUID_PATTERN+"}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}/neighbor")
    public Response storePhysicalLink(@PathParam("element_id") ElementId id, 
                                      @PathParam("ifp_name") InterfaceName ifpName,
                                      ElementPhysicalInterfaceNeighbor neighbor){
        service.storePhysicalInterfaceNeighbor(id,ifpName,neighbor);
        return success(messages);
    }
    
    @PUT
    @Path("/{element_name}/physical_interfaces/{ifp_name:"+IFP_PATTERN+"}")
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
    public Response storePhysicalInterfaces(@PathParam("element_id") ElementId id, 
                                            List<ElementPhysicalInterfaceSubmission> ifps){
        service.storePhysicalInterfaces(id, ifps);
        return success(messages);
    }
    
    @PUT
    @Path("/{element_name}/physical_interfaces")
    public Response storePyhsicalInterfaces(@PathParam("element_name") ElementName name, 
                                            List<ElementPhysicalInterfaceSubmission> ifps){
        service.storePhysicalInterfaces(name, ifps);
        return success(messages);
    }
    
}
