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

import static io.leitstand.commons.UniqueKeyConstraintViolationException.key;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.model.RollbackExceptionUtil.givenRollbackException;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT_SETTINGS;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static io.leitstand.inventory.service.ReasonCode.IVT0904E_PLAFORM_NAME_ALREADY_IN_USE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
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
	public List<PlatformSettings> getPlatforms(@QueryParam("filter") String filter){
		return service.getPlatforms(filter);
	}
	
	@POST
	@Path("/")
	public Response addPlatform(@Valid PlatformSettings settings){
		return storePlatform(settings.getPlatformId(), 
							 settings);	
	}

	@GET
	@Path("/{platform:"+UUID_PATTERN+"}")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
	public PlatformSettings getPlatform(@Valid @PathParam("platform") PlatformId platformId){
		return service.getPlatform(platformId);
	}

	@GET
	@Path("/{platform}")
	@Scopes({IVT, IVT_READ, IVT_ELEMENT, IVT_ELEMENT_SETTINGS})
	public PlatformSettings getPlatform(@Valid @PathParam("platform") PlatformName platformName){
		return service.getPlatform(platformName);
	}

	
	@PUT
	@Path("/{platform:"+UUID_PATTERN+"}")
	public Response storePlatform(@Valid @PathParam("platform") PlatformId platformId, 
								  @Valid PlatformSettings settings){
		if(isDifferent(platformId, settings.getPlatformId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "platform", 
												   platformId, 
												   settings.getPlatformId());
		} 		
		try {
			if(service.storePlatform(settings)) {
				
				return created(messages,
							   "%s", 
							   settings.getPlatformId());
			}
			return success(messages);
		} catch (Exception e) {
			// Check whether role exist
			givenRollbackException(e)
			.whenEntityExists(() -> service.getPlatform(settings.getPlatformName()))
			.thenThrow(new UniqueKeyConstraintViolationException(IVT0904E_PLAFORM_NAME_ALREADY_IN_USE,
													    		 key("platform_name", settings.getPlatformName())));
			throw e;
		}
		
		
	}
	
	
	@PUT
	@Path("/{platform}")
	public Response storePlatforms(@Valid @PathParam("platform") PlatformName platformName, 
								   @Valid PlatformSettings settings){
		if(service.storePlatform(settings)) {
			return created(messages,
						   "%s", 
						   settings.getPlatformId());
		}
		return success(messages);
	}

	@DELETE
	@Path("/{platform:"+UUID_PATTERN+"}")
	public Response deletePlatform(@PathParam("platform") @Valid PlatformId platformId) {
		service.removePlatform(platformId);
		return success(messages);
	}
	
	@DELETE
	@Path("/{platform}")
	public Response deletePlatform(@PathParam("platform") @Valid PlatformName platformName) {
		service.removePlatform(platformName);
		return success(messages);
	}
	
}
