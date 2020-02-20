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
import static io.leitstand.inventory.rs.Scopes.IVT_IMAGE;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.PackageService;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.QualifiedPackageName;
import io.leitstand.inventory.service.Version;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_READ, IVT_IMAGE})
@Path("/packages")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PackageResource {

	@Inject
	private PackageService service;
	
	@GET
	public List<QualifiedPackageName> getPackages(){
		return service.getPackages();
	}
	
	@GET
	@Path("/{org}/{name}/{major}.{minor}-{patch}")
	public PackageVersionInfo getPackageDetails(@PathParam("org") String org, 
	                                             @PathParam("name")String name, 
	                                             @PathParam("major") int major, 
	                                             @PathParam("minor") int minor, 
	                                             @PathParam("patch") int patch){
		return service.getPackageVersion(org,name,new Version(major, minor, patch));
	}
	
}
