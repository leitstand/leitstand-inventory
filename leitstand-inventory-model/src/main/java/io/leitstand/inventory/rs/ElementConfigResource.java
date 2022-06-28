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
import static io.leitstand.commons.rs.Responses.seeOther;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT_CONFIG;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigRevisions;
import io.leitstand.inventory.service.ElementConfigService;
import io.leitstand.inventory.service.ElementConfigs;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.StoreElementConfigResult;
import io.leitstand.security.auth.Scopes;

@Resource
@Path("/elements")
@Scopes({IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementConfigResource {

	@Inject
	private Messages messages;
	
	@Inject
	private ElementConfigService service;
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/configs")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementConfigs findElementConfigs(@Valid @PathParam("element") ElementId elementId,
											 @Valid @QueryParam("filter") String filter) {
		return service.findElementConfigs(elementId,
										  filter);
	}
	
	@GET
	@Path("/{element}/configs")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementConfigs findElementConfigs(@Valid @PathParam("element") ElementName elementName,
											 @Valid @QueryParam("filter") String filter) {
		return service.findElementConfigs(elementName,
										  filter);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementConfig getElementConfig(@Valid @PathParam("element") ElementId elementId,
										  @Valid @PathParam("config_id") ElementConfigId configId){
		return service.getElementConfig(elementId,
										configId);
	}
	
	@GET
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementConfig getElementConfig(@Valid @PathParam("element") ElementName elementName,
									      @Valid @PathParam("config_id") ElementConfigId configId){
		return service.getElementConfig(elementName, 
										configId);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_name}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementConfigRevisions getElementConfigRevisions(@Valid @PathParam("element") ElementId elementId,
											                @Valid @PathParam("config_name") ElementConfigName configName) {
		return service.getElementConfigRevisions(elementId,
												 configName);
	}
	
	@GET
	@Path("/{element}/configs/{config_name}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementConfigRevisions getElementConfigRevisions(@Valid @PathParam("element") ElementName elementName,
	                                                        @Valid @PathParam("config_name") ElementConfigName configName) {
		return service.getElementConfigRevisions(elementName,
												 configName);
	}	
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}")
	public Response removeElementConfig(@Valid @PathParam("element") ElementId elementId,
									    @Valid @PathParam("config_id") ElementConfigId configId){
		service.removeElementConfig(elementId,configId);
		return success(messages);
	}	
	
	@DELETE
	@Path("/{id:"+UUID_PATTERN+"}/configs/{config_name}/")
	public Response removeElementConfigRevisions(@Valid @PathParam("id") ElementId id,
										         @Valid @PathParam("config_name") ElementConfigName configName){
		service.removeElementConfigRevisions(id,
									configName);
		return success(messages);
	}	
	
	@DELETE
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}")
	public Response removeElementConfig(@Valid @PathParam("element") ElementName elementName,
								        @Valid @PathParam("config_id") ElementConfigId configId){
		service.removeElementConfig(elementName,configId);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element}/configs/{config_name}")
	public Response removeElementConfigRevisions(@Valid @PathParam("element") ElementName elementName,
								                 @Valid @PathParam("config_name") ElementConfigName configName){
		service.removeElementConfigRevisions(elementName,
								    configName);
		return success(messages);
	}
	
	@POST
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}/restore")
	public Response restoreElementConfig(@Valid @PathParam("element") ElementId elementId,
									     @Valid @PathParam("config_id") ElementConfigId configId,
									     @QueryParam("comment") String comment){
		StoreElementConfigResult result = service.restoreElementConfig(elementId, 
																	   configId,
																	   comment);
		if(result.isCreated()) {
			return created(messages, result.getConfigId());
		}
		
		return seeOther(result.getConfigId(),r->r.entity(messages));
	}
	
	@POST
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}/restore")
	public Response restoreElementConfig(@Valid @PathParam("element") ElementName elementName,
									     @Valid @PathParam("config_id") ElementConfigId configId,
									     @QueryParam("comment") String comment){
		StoreElementConfigResult result = service.restoreElementConfig(elementName, 
																	   configId,
																	   comment);
		if(result.isCreated()) {
			return created(messages, result.getConfigId());
		}

		return seeOther(result.getConfigId(),r->r.entity(messages));
	}
	
	protected static String ext(ElementConfig config) {
		if(config.getContentType().contains("json")) {
			return "json";
		}
		if(config.getContentType().contains("yaml")||config.getContentType().contains("yml")){
			return "yaml";
		}
		if(config.getContentType().contains("xml")){
			return "xml";
		}
		return "txt";
	}
		
	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}/comment")
	public Messages storeElementConfigComment(@Valid @PathParam("element") ElementId elementId, 
											  @Valid @PathParam("config_id") ElementConfigId configId,
											  String comment){
		
		service.setElementConfigComment(elementId,
										configId,
										comment);
		return messages;
	}
	
	@PUT
	@Path("/{element}/configs/{config_name}/{config_id:"+UUID_PATTERN+"}/comment")
	public Messages storeElementConfigComment(@Valid @PathParam("element") ElementName elementName, 
											  @Valid @PathParam("config_id") ElementConfigId configId,
											  String comment){

		service.setElementConfigComment(elementName,
										configId,
										comment);
		return messages;
	}
	
	
}
