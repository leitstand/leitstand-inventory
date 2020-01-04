/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.jsonb.IsoDateAdapter.isoDateFormat;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.seeOther;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.security.auth.Role.OPERATOR;
import static io.leitstand.security.auth.Role.SYSTEM;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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


@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementConfigResource {

	
	@Inject
	private Messages messages;
	
	@Inject
	private ElementConfigService service;
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/configs")
	public ElementConfigs findElementConfigs(@Valid @PathParam("element") ElementId elementId,
											 @Valid @QueryParam("filter") String filter) {
		return service.findElementConfigs(elementId,
										  filter);
	}
	
	@GET
	@Path("/{element}/configs")
	public ElementConfigs findElementConfigs(@Valid @PathParam("element") ElementName elementName,
											 @Valid @QueryParam("filter") String filter) {
		return service.findElementConfigs(elementName,
										  filter);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}")
	public ElementConfig getElementConfig(@Valid @PathParam("element") ElementId elementId,
										  @Valid @PathParam("config_id") ElementConfigId configId){
		return service.getElementConfig(elementId,
										configId);
	}
	
	@GET
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}")
	public ElementConfig getElementConfig(@Valid @PathParam("element") ElementName elementName,
									      @Valid @PathParam("config_id") ElementConfigId configId){
		return service.getElementConfig(elementName, 
										configId);
	}
	
	@GET
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}/config")
	public Response getElementConfigData(@Valid @PathParam("element") ElementName elementName,
					  		      	  	 @Valid @PathParam("config_id") ElementConfigId configId){
		ElementConfig config = service.getElementConfig(elementName,
														configId);
		
		return success(config.getConfig().toString(), 
				  	   config.getContentType());
	}	
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_name}")
	public ElementConfigRevisions getRevisions(@Valid @PathParam("element") ElementId elementId,
											   @Valid @PathParam("config_name") ElementConfigName configName) {
		return service.getElementConfigRevisions(elementId,
												 configName);
	}
	
	@GET
	@Path("/{element}/configs/{config_name}")
	public ElementConfigRevisions getRevisions(@Valid @PathParam("element") ElementName elementName,
											   @Valid @PathParam("config_name") ElementConfigName configName) {
		return service.getElementConfigRevisions(elementName,
												 configName);
	}	
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElementConfig(@Valid @PathParam("element") ElementId elementId,
									    @Valid @PathParam("config_id") ElementConfigId configId){
		service.removeElementConfig(elementId,configId);
		return success(messages);
	}	
	
	@DELETE
	@Path("/{id:"+UUID_PATTERN+"}/configs/{config_name}/")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElementConfig(@Valid @PathParam("id") ElementId id,
										@Valid @PathParam("config_name") ElementConfigName configName){
		service.removeElementConfig(id,
									configName);
		return success(messages);
	}	
	
	@DELETE
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElementConfig(@Valid @PathParam("element") ElementName elementName,
								        @Valid @PathParam("config_id") ElementConfigId configId){
		service.removeElementConfig(elementName,configId);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element}/configs/{config_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeElementConfig(@Valid @PathParam("element") ElementName elementName,
								        @Valid @PathParam("config_name") ElementConfigName configName){
		service.removeElementConfig(elementName,
								    configName);
		return success(messages);
	}
	
	@POST
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}/_edit")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response editElementConfig(@Valid @PathParam("element") ElementId elementId,
									  @Valid @PathParam("config_id") ElementConfigId configId,
									  @QueryParam("comment") String comment){
		StoreElementConfigResult result = service.editElementConfig(elementId, 
																	configId,
																	comment);
		if(result.isCreated()) {
			return created(result.getConfigId());
		}
		return seeOther(result.getConfigId());
	}
	
	@POST
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}/_edit")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response editElementConfig(@Valid @PathParam("element") ElementName elementName,
									  @Valid @PathParam("config_id") ElementConfigId configId,
									  @QueryParam("comment") String comment){
		StoreElementConfigResult result = service.editElementConfig(elementName, 
																	configId,
																	comment);
		if(result.isCreated()) {
			return created(result.getConfigId());
		}
		return seeOther(result.getConfigId());
	}
	
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_id:"+UUID_PATTERN+"}/config")
	public Response downloadElementConfig(@Valid @PathParam("element") ElementId elementId,
										  @Valid @PathParam("config_id") ElementConfigId configId){
		
		ElementConfig config = service.getElementConfig(elementId, configId);
		
		return ok(config.getConfig(), config.getContentType())
			   .header("Content-Disposition", format("attachment; filename=\"%s_%s.%s\"",
					   								 config.getConfigName(),
					   								 isoDateFormat(config.getDateModified()),
					   								 ext(config)))
			   .build();
		
	}
	
	
	
	@GET
	@Path("/{element}/configs/{config_id:"+UUID_PATTERN+"}/config")
	public Response downloadElementConfig(@Valid @PathParam("element") ElementName elementName,
								     	  @Valid @PathParam("config_id") ElementConfigId configId){
		ElementConfig config = service.getElementConfig(elementName, configId);
		
		return ok(config.getConfig(), config.getContentType())
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
		if(config.getContentType().contains("yaml")){
			return "yaml";
		}
		if(config.getContentType().contains("xml")){
			return "xml";
		}
		return "txt";
	}
	
	@POST
	@Path("/{element:"+UUID_PATTERN+"}/configs/{config_name}")
	@RolesAllowed({OPERATOR,SYSTEM})
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
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeElementConfig(@Valid @PathParam("element") ElementName elementName, 
									   @Valid @PathParam("config_name") ElementConfigName configName,
									   @NotNull @HeaderParam("Content-Type") String contentType,
									   @QueryParam("state") @DefaultValue("ACTIVE") ConfigurationState state,
									   String config,
									   @QueryParam("comment") String comment){

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
	@RolesAllowed({OPERATOR,SYSTEM})
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
	@RolesAllowed({OPERATOR,SYSTEM})
	public Messages storeElementConfigComment(@Valid @PathParam("element") ElementName elementName, 
											  @Valid @PathParam("config_id") ElementConfigId configId,
											  String comment){

			service.setElementConfigComment(elementName,
											configId,
											comment);
			return messages;
	}
	
	
}
