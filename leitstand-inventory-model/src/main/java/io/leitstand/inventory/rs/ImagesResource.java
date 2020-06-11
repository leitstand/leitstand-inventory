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
import static io.leitstand.inventory.rs.Scopes.IVT_IMAGE;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageReference;
import io.leitstand.inventory.service.ImageService;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageStatistics;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.RoleImages;
import io.leitstand.inventory.service.Version;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_IMAGE})
@Path("/images")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ImagesResource {

	@Inject
	private ImageService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Scopes({IVT, IVT_READ, IVT_IMAGE})
	public List<ImageReference> findImages(@QueryParam("filter") @DefaultValue("") String filter, 
										   @QueryParam("element_role") @Valid ElementRoleName elementRole,
										   @QueryParam("image_type") ImageType type,
										   @QueryParam("image_state") ImageState state,
										   @QueryParam("image_version") @Valid Version version){
			return service.findImages(filter, 
							          elementRole,
							    	  type,
							    	  state,
							    	  version,
							    	  100);
	}
	
	@GET
	@Path("/{element_role}")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})
	public RoleImages findRoleImages(@PathParam("element_role") ElementRoleName elementRole) {
		return service.findRoleImages(elementRole);
	}
	
	
	@POST
	@Path("/")
	public Response storeImage(@Valid ImageInfo image) {
		if(service.storeImage(image)) {
			return created(messages,"/images/%s",image.getImageId());
		}
		return success(messages);
	}
	
	@PUT
	@Path("/{image_id:"+UUID_PATTERN+"}")
	public Response storeImage(@PathParam("image_id") ImageId id, 
							   @Valid ImageInfo image){
		if(isDifferent(id, image.getImageId())){
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "image_id",
												   id,
												   image.getImageId());
		}
		
		if(service.storeImage(image)) {
			return created(messages,"/images/%s",image.getImageId());
		}
		return success(messages);
	}
	
	@PUT
	@Path("/{image_id:"+UUID_PATTERN+"}/image_state")
	public Response storeImage(@PathParam("image_id") ImageId id, ImageState state){
		service.updateImageState(id,state);
		return success(messages);
	}
	
	@GET
	@Path("/{image_id:"+UUID_PATTERN+"}")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})

	public ImageInfo getImage(@PathParam("image_id") ImageId id){
		return service.getImage(id);
	}

	@GET
	@Path("/{image_id:"+UUID_PATTERN+"}/statistics")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})

	public ImageStatistics getImageStatistics(@PathParam("image_id") ImageId id){
		return service.getImageStatistics(id);
	}
	
	@DELETE
	@Path("/{image_id:"+UUID_PATTERN+"}")
	public Response removeImage(@PathParam("image_id") ImageId id){
		service.removeImage(id);
		return success(messages);
	}
	
	@GET
	@Path("/_types")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})
	public List<ImageType> getImageTypes() {
		return service.getImageTypes();
	}
	
	@GET
	@Path("/_versions")
	@Scopes({IVT, IVT_READ, IVT_IMAGE})
	public List<Version> getImageVersions(@QueryParam("image_type") @Valid ImageType imageType) {
		return service.getImageVersions(imageType);
	}
	
}
