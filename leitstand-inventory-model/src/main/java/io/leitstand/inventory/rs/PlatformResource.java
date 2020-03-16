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
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT_SETTINGS;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.ok;

import java.net.URI;
import java.util.List;

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
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformService;
import io.leitstand.inventory.service.PlatformSettings;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
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
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
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
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
	public PlatformSettings getPlatforms(@Valid @PathParam("platform_id") PlatformId platformId){
		return service.getPlatform(platformId);
	}
	
	@GET
	@Path("/{vendor}")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
	public List<PlatformSettings> getPlatforms(@PathParam("vendor") String vendor){
		return service.getPlatforms(vendor);
	}
	
	@GET
	@Path("/{vendor}/{model}")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
	public PlatformSettings getPlatform(@PathParam("vendor") String vendor, 
									    @PathParam("model") String model) {
		return service.getPlatform(vendor,model);
	}

	@PUT
	@Path("/{platform_id:"+UUID_PATTERN+"}")
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
	public Response deletePlatform(@PathParam("platform_id") @Valid PlatformId platformId) {
		service.removePlatform(platformId);
		if(messages.size() > 0) {
			return ok(messages).build();
		}
		return noContent().build();
	}
	
}
