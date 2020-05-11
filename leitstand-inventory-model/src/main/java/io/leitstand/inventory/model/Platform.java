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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import io.leitstand.commons.jpa.BooleanConverter;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.PlatformNameConverter;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;

@Entity
@Table(schema="inventory",
	   name="platform",
	   uniqueConstraints=@UniqueConstraint(columnNames= {"vendor","model"}))
@NamedQueries({
	@NamedQuery(name="Platform.findByPlatformId", 
				query="SELECT p FROM Platform p WHERE p.uuid=:uuid"),
	@NamedQuery(name="Platform.findByPlatformName", 
				query="SELECT p FROM Platform p WHERE p.name=:name"),
	@NamedQuery(name="Platform.findAll", 
				query="SELECT p FROM Platform p ORDER BY p.name"),
	@NamedQuery(name="Platform.findMatches", 
				query="SELECT p FROM Platform p WHERE CAST(p.name as TEXT) REGEXP :filter OR p.vendor REGEXP :filter OR p.model REGEXP :filter ORDER BY p.name" ),	
	@NamedQuery(name="Platform.findByElementGroupAndElementRole", 
				query="SELECT p FROM Element e JOIN e.platform p WHERE e.group=:group AND e.role=:role "),
	@NamedQuery(name="Platform.countElements",
				query="SELECT count(e) FROM Element e WHERE e.platform=:platform")
	
})
public class Platform extends VersionableEntity{

	private static final long serialVersionUID = 1L;
	
	public static Query<Long> countElements(Platform platform){
		return em -> em.createNamedQuery("Platform.countElements",Long.class)
					.setParameter("platform", platform)
					.getSingleResult();
	}

	public static Query<List<Platform>> findAll(String filter) {
		if(isEmptyString(filter) || ".*".equals(filter)) {
			return em -> em.createNamedQuery("Platform.findAll",Platform.class)
					   	   .getResultList();		
		}
		return em -> em.createNamedQuery("Platform.findMatches",Platform.class)
					   .setParameter("filter",filter)
					   .getResultList();
		
	}
	
	public static Query<Platform> findByPlatformName(PlatformName name){
		return em -> em.createNamedQuery("Platform.findByPlatformName",Platform.class)
					   .setParameter("name", name)
					   .getSingleResult();
	}
	
	public static Query<List<Platform>> findByElementGroupAndElementRole(ElementGroup group, ElementRole role){
		return em -> em.createNamedQuery("Platform.findByElementGroupAndElementRole",Platform.class)
					   .setParameter("group", group)
					   .setParameter("role", role)
					   .getResultList();
	}
	
	public static Query<Platform> findByPlatformId(PlatformId platformId){
		return em -> em.createNamedQuery("Platform.findByPlatformId", Platform.class)
					   .setParameter("uuid", platformId.toString())
					   .getSingleResult();
	}
	
	@Convert(converter = PlatformNameConverter.class)
	@Column(unique=true)
	private PlatformName name;
	private String vendor;
	private String model;
	private String description;	
	private int rackUnits;
	@Convert(converter=BooleanConverter.class)
	private boolean halfRack;

	protected Platform() {
		// JPA
	}
	
	public Platform(PlatformId platformId, PlatformName platformName) {
		super(platformId.toString());
		this.name = platformName;
		this.rackUnits = 1;
	}
	
	public PlatformName getPlatformName() {
		return name;
	}
	
	public String getVendor() {
		return vendor;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	void setPlatformName(PlatformName platformName) {
		this.name = platformName;
	}
	
	void setVendor(String vendor) {
		this.vendor = vendor;
	}

	
	void setRackUnits(int rackUnits) {
		this.rackUnits = rackUnits;
	}
	
	public int getHeight() {
		return this.rackUnits;
	}
	
	public void setHalfRack(boolean halfRack) {
		this.halfRack = halfRack;
	}
	
	public boolean isHalfRackSize() {
		return this.halfRack;
	}

	public PlatformId getPlatformId() {
		return PlatformId.valueOf(getUuid());
	}

}
