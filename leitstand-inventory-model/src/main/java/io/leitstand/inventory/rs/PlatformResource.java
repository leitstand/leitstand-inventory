/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.security.auth.Role.ADMINISTRATOR;
import static io.leitstand.security.auth.Role.OPERATOR;
import static io.leitstand.security.auth.Role.SYSTEM;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;

import java.net.URI;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
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
import javax.ws.rs.core.Response;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformService;
import io.leitstand.inventory.service.PlatformSettings;

@RequestScoped
@Path("/platforms")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PlatformResource {

	@Inject
	private PlatformService service;
	@Inject
	private Messages messages;
	
	@GET
	@Path("/")
	public List<PlatformSettings> getPlatforms(){
		return service.getPlatforms();
	}
	
	@POST
	@Path("/")
	public Response addPlatform(@Valid PlatformSettings settings){
		return storePlatforms(settings.getPlatformId(), 
							  settings);	
	}

	@GET
	@Path("/{platform_id:"+UUID_PATTERN+"}")
	public PlatformSettings getPlatforms(@Valid @PathParam("platform_id") PlatformId platformId){
		return service.getPlatform(platformId);
	}
	
	@GET
	@Path("/{vendor}")
	public List<PlatformSettings> getPlatforms(@PathParam("vendor") String vendor){
		return service.getPlatforms(vendor);
	}
	
	@GET
	@Path("/{vendor}/{model}")
	public PlatformSettings getPlatform(@PathParam("vendor") String vendor, 
									    @PathParam("model") String model) {
		return service.getPlatform(vendor,model);
	}

	@PUT
	@Path("/{platform_id:"+UUID_PATTERN+"}")
	@RolesAllowed({OPERATOR,ADMINISTRATOR,SYSTEM})
	public Response storePlatforms(@Valid @PathParam("platform_id") PlatformId platformId, 
								   @Valid PlatformSettings settings){
		if(isDifferent(platformId, settings.getPlatformId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "platform_id", 
												   platformId, 
												   settings.getPlatformId());
		}		
		if(service.storePlatform(settings)) {
			return created(URI.create(format("%s",settings.getPlatformId())))
				   .entity(messages)
				   .build();
		}
		return ok(messages)
				.build();
	}

	@DELETE
	@Path("/{platform_id:"+UUID_PATTERN+"}")
	@RolesAllowed({OPERATOR,ADMINISTRATOR,SYSTEM})
	public Response deletePlatform(@PathParam("platform_id") @Valid PlatformId platformId) {
		service.removePlatform(platformId);
		if(messages.size() > 0) {
			return ok(messages).build();
		}
		return noContent().build();
	}
	
}
