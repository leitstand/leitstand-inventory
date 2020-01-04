/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import io.leitstand.inventory.service.PackageService;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.QualifiedPackageName;
import io.leitstand.inventory.service.Version;

@RequestScoped
@Path("/packages")
//@RolesAllowed("user")
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
