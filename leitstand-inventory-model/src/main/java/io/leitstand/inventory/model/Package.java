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
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ReasonCode.IVT0511E_PACKAGE_VERSION_EXISTS;
import static java.util.Collections.unmodifiableList;
import static javax.persistence.CascadeType.ALL;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.LockModeType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.service.Version;

@Entity
@Table(schema="inventory", name="package")
@NamedQuery(name="Package.findByName", 
			query="SELECT p FROM Package p WHERE p.org=:org AND p.name=:name AND p.ext=:ext")
public class Package extends VersionableEntity{

	private static final long serialVersionUID = 1L;

	public static Query<Package> findByName(String org, String name, String ext, LockModeType locking) {
		return em -> em.createNamedQuery("Package.findByName",Package.class)
					   .setParameter("org",org)
					   .setParameter("name",name)
					   .setParameter("ext", ext)
					   .setLockMode(locking)
					   .getSingleResult();
		
	}
	
	@Column(nullable=false, length=64)
	private String org; // e.g. net.rtbrick.bgp , e.g. net.rtbrick.isis

	@Column(nullable=false, length=64)
	private String name;
	
	@Column(nullable=false, length=16)
	private String ext;
	
	@OneToMany(cascade=ALL, orphanRemoval=true, mappedBy="pkg")
	private List<Package_Version> versions;
	
	protected Package(){
		//JPA
	}
	
	public Package(String org, String name, String ext){
		this.org = org;
		this.name = name;
		this.ext = ext;
		this.versions = new LinkedList<>();
	}
	
	public String getOrganization() {
		return org;
	}
	
	public String getPackageName() {
		return name;
	}
	
	Package_Version newVersion(Version rev){
		if(findVersion(rev) != null){
			throw new ConflictException(IVT0511E_PACKAGE_VERSION_EXISTS,getOrganization(),getPackageName(),rev);
		}
		Package_Version revision = new Package_Version(this,rev);
		versions.add(revision);
		return revision;
	}
	
	void removeVersion(Version rev){
		Package_Version version = findVersion(rev);
		if(version != null){
			versions.remove(version);
		}
	}
	
	private Package_Version findVersion(Version rev){
		for(Package_Version revision : getVersions()){
			if(revision.getPackageVersion().equals(rev)){
				return revision;
			}
		}
		return null;
	}
	
	List<Package_Version> getVersions(){
		return unmodifiableList(versions);
	}

	public String getPackageExtension() {
		return ext;
	}

}
