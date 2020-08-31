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

import static io.leitstand.commons.jsonb.IsoDateAdapter.isoDateFormat;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.seeOther;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT_CONFIG;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static java.lang.String.format;
import static javax.json.Json.createWriterFactory;
import static javax.json.stream.JsonGenerator.PRETTY_PRINTING;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ConfigurationState;
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

    private static final Logger LOG = Logger.getLogger(ElementConfigResource.class.getName());
	
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
	public ElementConfigRevisions getRevisions(@Valid @PathParam("element") ElementId elementId,
											   @Valid @PathParam("config_name") ElementConfigName configName) {
		return service.getElementConfigRevisions(elementId,
												 configName);
	}
	
	@GET
	@Path("/{element}/configs/{config_name}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public ElementConfigRevisions getRevisions(@Valid @PathParam("element") ElementName elementName,
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
	public Response removeElementConfig(@Valid @PathParam("id") ElementId id,
										@Valid @PathParam("config_name") ElementConfigName configName){
		service.removeElementConfig(id,
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
	public Response removeElementConfig(@Valid @PathParam("element") ElementName elementName,
								        @Valid @PathParam("config_name") ElementConfigName configName){
		service.removeElementConfig(elementName,
								    configName);
		return success(messages);
	}
	
	@POST
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}/_restore")
	public Response restoreElementConfig(@Valid @PathParam("element") ElementId elementId,
									     @Valid @PathParam("config_id") ElementConfigId configId,
									     @QueryParam("comment") String comment){
		StoreElementConfigResult result = service.restoreElementConfig(elementId, 
																	   configId,
																	   comment);
		if(result.isCreated()) {
			return created(result.getConfigId());
		}
		return seeOther(result.getConfigId());
	}
	
	@POST
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}/_restore")
	public Response restoreElementConfig(@Valid @PathParam("element") ElementName elementName,
									     @Valid @PathParam("config_id") ElementConfigId configId,
									     @QueryParam("comment") String comment){
		StoreElementConfigResult result = service.restoreElementConfig(elementName, 
																	   configId,
																	   comment);
		if(result.isCreated()) {
			return created(result.getConfigId());
		}
		return seeOther(result.getConfigId());
	}
	
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}/config")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public Response downloadElementConfig(@Valid @PathParam("element") ElementId elementId,
										  @Valid @PathParam("config_id") ElementConfigId configId,
										  @QueryParam("pretty") boolean pretty){
		
		ElementConfig config = service.getElementConfig(elementId, configId);
		
		return ok(formattedConfig(config,pretty), config.getContentType())
			   .header("Content-Disposition", format("attachment; filename=\"%s_%s.%s\"",
					   								 config.getConfigName(),
					   								 isoDateFormat(config.getDateModified()),
					   								 ext(config)))
			   .build();
		
	}
	
	
	
	@GET
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}/config")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_CONFIG})
	public Response downloadElementConfig(@Valid @PathParam("element") ElementName elementName,
								     	  @Valid @PathParam("config_id") ElementConfigId configId,
								     	  @QueryParam("pretty") boolean pretty){
		ElementConfig config = service.getElementConfig(elementName, configId);
		
		return ok(formattedConfig(config, pretty), config.getContentType())
			   .header("Content-Disposition", format("attachment; filename=\"%s_%s.%s\"",
					   								 config.getConfigName(),
					   								 isoDateFormat(config.getDateModified()),
					   								 ext(config)))
			   .build();
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
	
	protected static Object formattedConfig(ElementConfig config, boolean prettyPrint) {
	    if(prettyPrint && config.getContentType().contains("json")) {
	        Map<String,Object> settings = new HashMap<>();
	        settings.put(PRETTY_PRINTING, true);
	        try(StringWriter buffer = new StringWriter();
	            JsonWriter prettyPrinter = createWriterFactory(settings)
	                                       .createWriter(buffer)){
	            JsonObject object = (JsonObject) config.getConfig();
	            prettyPrinter.writeObject(object);
	            return buffer.toString();
	            
	        } catch (IOException e) {
	            LOG.fine(() -> "Failed to format JSON configuration due to IO exception: "+e);
	            // Use default implementation to format the output
	        }
	        
	    }
	    return config.getConfig();
	    
	}
	
	@POST
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_name}")
	@Consumes({"text/*","application/json","application-vmd/yaml","application/xml"})
	public Response storeElementConfig(@Valid @PathParam("element") ElementId elementId, 
									   @Valid @PathParam("config_name") ElementConfigName configName,
									   @NotNull @HeaderParam("Content-Type") String contentType,
									   @QueryParam("state") @DefaultValue("ACTIVE") ConfigurationState state,
									   @QueryParam("comment") String comment,
									   String config){ 
		
		
		StoreElementConfigResult result = service.storeElementConfig(elementId, 
								   									 configName, 
								   									 MediaType.valueOf(contentType), 
								   									 state,
								   									 config,
								   									 comment);
		
		
		if(result.isCreated()) {
			return created(messages,result.getConfigId());
		}
		return success(messages);
	}
	
	@POST
	@Path("/{element}/configs/{config_name}")
	@Consumes({"text/*","application/json","application-vmd/yaml","application/xml"})
	public Response storeElementConfig(@Valid @PathParam("element") ElementName elementName, 
									   @Valid @PathParam("config_name") ElementConfigName configName,
									   @NotNull @HeaderParam("Content-Type") String contentType,
									   @QueryParam("state") @DefaultValue("ACTIVE") ConfigurationState state,
									   @QueryParam("comment") String comment,
									   String config){

		StoreElementConfigResult result = service.storeElementConfig(elementName, 
					 												 configName, 
					 												 MediaType.valueOf(contentType), 
					 												 state,
					 												 config,
					 												 comment);

		if(result.isCreated()) {
			return created(messages,
						   result.getConfigId());
		}
		return success(messages);
				
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
