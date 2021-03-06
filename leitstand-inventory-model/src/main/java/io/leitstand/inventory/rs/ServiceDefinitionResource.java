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

import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.ServiceDefinition;
import io.leitstand.inventory.service.ServiceDefinitionService;
import io.leitstand.security.auth.Scopes;

@Resource
@Path("/services")
@Scopes({IVT, IVT_READ, IVT_ELEMENT})

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ServiceDefinitionResource {

	@Inject
	private ServiceDefinitionService service;
	
	@GET
	public List<ServiceDefinition> getServices(){
		return service.getServices();
	}
	
	
	
}
