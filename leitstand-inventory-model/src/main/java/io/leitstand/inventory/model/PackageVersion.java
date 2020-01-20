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

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.service.Version;

@Entity
@Table(schema="inventory",
	   name="packageversion")
@NamedQuery(name="PackageVersion.findByNameAndVersion", 
			query="SELECT p FROM PackageVersion p "+
				  "JOIN FETCH p.pkg "+
				  "WHERE p.pkg.org=:org "+
				  "AND p.pkg.name=:name "+
				  "AND p.major=:major "+
				  "AND p.minor=:minor "+
				  "AND p.patch=:patch "+
				  "AND p.prerelease IS NULL")
@NamedQuery(name="PackageVersion.findByNameAndPreReleaseVersion", 
			query="SELECT p FROM PackageVersion p "+
				  "JOIN FETCH p.pkg "+
				  "WHERE p.pkg.org=:org "+
				  "AND p.pkg.name=:name "+
				  "AND p.major=:major "+
				  "AND p.minor=:minor "+
				  "AND p.patch=:patch "+
				  "AND p.prerelease=:prerelease")
public class PackageVersion extends VersionableEntity {
	
	private static final long serialVersionUID = 1L;

	public static Query<PackageVersion> findPackageVersion(String org, String name, Version rev) {
		if(isEmptyString(rev.getPreRelease())) {
			return em -> em.createNamedQuery("PackageVersion.findByNameAndVersion",
											 PackageVersion.class)
						   .setParameter("org",org)
						   .setParameter("name", name)
						   .setParameter("major",rev.getMajorLevel())
						   .setParameter("minor",rev.getMinorLevel())
						   .setParameter("patch",rev.getPatchLevel())
						   .getSingleResult();
		}
		return em -> em.createNamedQuery("PackageVersion.findByNameAndPreReleaseVersion",
				 						 PackageVersion.class)
					   .setParameter("org",org)
					   .setParameter("name", name)
					   .setParameter("major",rev.getMajorLevel())
					   .setParameter("minor",rev.getMinorLevel())
					   .setParameter("patch",rev.getPatchLevel())
					   .setParameter("prerelease",rev.getPreRelease())
					   .getSingleResult();
	}

	@ManyToOne
	@JoinColumn(name="package_id", nullable=false)
	private Package pkg;

	private int major;
	private int minor;
	private int patch;
	private String prerelease;
	
	@Temporal(TIMESTAMP)
	@Column(name="tsbuild")
	private Date buildDate;
	@Column // TODO Length
	private String buildId;
	@ElementCollection
	@CollectionTable(schema="inventory",
					 name="package_revision_checksum",
					 joinColumns=@JoinColumn(name="package_revision_id"))
	private List<Checksum> checksums = emptyList();
	
	
	protected PackageVersion(){
		// JPA
	}
	
	public PackageVersion(Package pkg, Version rev){
		this.pkg = pkg;
		this.major = rev.getMajorLevel();
		this.minor = rev.getMinorLevel();
		this.patch = rev.getPatchLevel();
		this.prerelease = rev.getPreRelease();
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
	
	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}
	
	public void setChecksums(List<Checksum> checksums) {
		this.checksums = new ArrayList<>(checksums);
	}
	
	boolean isRevisionOfSamePackage(PackageVersion revision){
		return this.pkg.getId().equals(revision.pkg.getId());
	}
	
	boolean isSame(PackageVersion revision){
		if(!isRevisionOfSamePackage(revision)){
			return false;
		}
		return revision.getPackageVersion().equals(getPackageVersion());		
	}

	public List<Checksum> getChecksums() {
		return unmodifiableList(checksums);
	}
	
	public Version getPackageVersion(){
		return new Version(major,
						   minor,
						   patch,
						   prerelease);
	}
	
	public Date getBuildDate() {
		if(buildDate == null) {
			return null;
		}
		return new Date(buildDate.getTime());
	}
	
	public String getBuildId() {
		return buildId;
	}
	
	public String getOrganization(){
		return getPackage().getOrganization();
	}
	
	public String getPackageName(){
		return getPackage().getPackageName();
	}

	public String getPackageExtension() {
		return pkg.getPackageExtension();
	}
	
	Package getPackage() {
		return pkg;
	}


}
