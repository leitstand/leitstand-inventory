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
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_IMAGE;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementImagesService;
import io.leitstand.inventory.service.ElementInstalledImage;
import io.leitstand.inventory.service.ElementInstalledImageReference;
import io.leitstand.inventory.service.ElementInstalledImages;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_IMAGE, IVT_ELEMENT})

@Path("/elements")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementImageResource{

	
	@Inject
	private ElementImagesService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/images/{image}")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})

	public ElementInstalledImage getElementInstalledImage(@Valid @PathParam("element") ElementId elementId, 
	                                                      @Valid @PathParam("image") ImageId imageId){
		return service.getElementInstalledImage(elementId,
												imageId);
	}
	
	@GET
	@Path("/{element}/images/{image}")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})
	public ElementInstalledImage getElementInstalledImage(@Valid @PathParam("element") ElementName elementName, 
	                                                      @Valid @PathParam("image") ImageId imageId){
		return service.getElementInstalledImage(elementName,imageId);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/images")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})
	public ElementInstalledImages getElementInstalledImages(@Valid @PathParam("element") ElementId elementId){
		return service.getElementInstalledImages(elementId);
	}

	@GET
	@Path("/{element}/images")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})
	public ElementInstalledImages getElementInstalledImages(@Valid @PathParam("element") ElementName elementName){
		return service.getElementInstalledImages(elementName);
	}
	
	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/images")
	public Response storeInstalledImages(@Valid @PathParam("element") ElementId elementId, 
	                                     @Valid List<ElementInstalledImageReference> images){
		service.storeInstalledImages(elementId, images);
		return success(messages);
	}
	
	@PUT
	@Path("/{element}/images")
	public Response storeInstalledImages(@Valid @PathParam("element") ElementName elementName, 
	                                     @Valid List<ElementInstalledImageReference> images){
		service.storeInstalledImages(elementName, images);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}/images/{image:"+UUID_PATTERN+"}")
	public Response removeInstalledImage(@Valid @PathParam("element") ElementId elementId, 
										 @Valid @PathParam("image") ImageId imageId){
		service.removeInstalledImage(elementId, imageId);
		return success(messages);

	}
	
	@DELETE
	@Path("/{name}/images/{image:"+UUID_PATTERN+"}")
	public Response removeCachedImages(@Valid @PathParam("name") ElementName elementName, 
	                                   @Valid @PathParam("image") ImageId imageId){
		service.removeInstalledImage(elementName, imageId);
		return success(messages);
	}
	
}
