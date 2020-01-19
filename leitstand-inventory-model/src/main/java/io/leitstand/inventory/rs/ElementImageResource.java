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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementImagesService;
import io.leitstand.inventory.service.ElementInstalledImage;
import io.leitstand.inventory.service.ElementInstalledImageReference;
import io.leitstand.inventory.service.ElementInstalledImages;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ImageId;

@RequestScoped
@Path("/elements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ElementImageResource{

	
	@Inject
	private ElementImagesService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/images/{image_id}")
	public ElementInstalledImage getElementInstalledImage(@Valid @PathParam("id") ElementId id, 
	                                                                  @Valid @PathParam("image_id") ImageId imageId){
		return service.getElementInstalledImage(id,imageId);
	}
	
	@GET
	@Path("/{name}/images/{image_id}")
	public ElementInstalledImage getElementInstalledImage(@Valid @PathParam("name") ElementName name, 
	                                                                  @Valid @PathParam("image_id") ImageId imageId){
		return service.getElementInstalledImage(name,imageId);
	}
	
	@GET
	@Path("/{id:"+UUID_PATTERN+"}/images")
	public ElementInstalledImages getElementInstalledImages(@Valid @PathParam("id") ElementId id){
		return service.getElementInstalledImages(id);
	}

	@GET
	@Path("/{name}/images")
	public ElementInstalledImages getElementInstalledImages(@Valid @PathParam("name") ElementName name){
		return service.getElementInstalledImages(name);
	}
	
	@PUT
	@Path("/{id:"+UUID_PATTERN+"}/images")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeInstalledImages(@Valid @PathParam("id") ElementId id, 
	                                       @Valid List<ElementInstalledImageReference> images){
		service.storeInstalledImages(id, images);
		return success(messages);
	}
	
	@PUT
	@Path("/{name}/images")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeInstalledImages(@Valid @PathParam("name") ElementName name, 
	                                       @Valid List<ElementInstalledImageReference> images){
		service.storeInstalledImages(name, images);
		return success(messages);
	}
	
	@POST
	@Path("/{id:"+UUID_PATTERN+"}/images")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeCachedImages(@Valid @PathParam("id") ElementId id, 
	                                  @Valid List<ElementInstalledImageReference> images){
		service.addCachedImages(id, images);
		return success(messages);
	}
	
	@POST
	@Path("/{name}/images")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response storeCachedImages(@Valid @PathParam("name") ElementName name, 
	                                  @Valid List<ElementInstalledImageReference> images){
		service.addCachedImages(name, images);
		return success(messages);
	}

	@DELETE
	@Path("/{id:"+UUID_PATTERN+"}/images")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeCachedImages(@Valid @PathParam("id") ElementId id, 
	                                   @Valid List<ElementInstalledImageReference> images){
		service.removeCachedImages(id, images);
		return success(messages);

	}
	
	@DELETE
	@Path("/{name}/images")
	@RolesAllowed({OPERATOR,SYSTEM})
	public Response removeCachedImages(@Valid @PathParam("name") ElementName name, 
	                                   @Valid List<ElementInstalledImageReference> images){
		service.removeCachedImages(name, images);
		return success(messages);
	}
	
}
